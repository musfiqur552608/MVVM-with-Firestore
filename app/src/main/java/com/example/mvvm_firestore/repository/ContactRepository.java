package com.example.mvvm_firestore.repository;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.mvvm_firestore.model.ContactUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactRepository {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public MutableLiveData<String> insertContactFirestore(ContactUser user, Uri uri){
        MutableLiveData<String> insertResultLiveData = new MutableLiveData<>();
        String currentUser = firebaseAuth.getCurrentUser().getUid();

        StorageReference imagePath = storageReference.child("profile_image").child(currentUser).child(user.getContactId()+"jpg");
        imagePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Map<String, String>contactMap = new HashMap<>();
                        contactMap.put("contact_Id", user.getContactId());
                        contactMap.put("contact_Name", user.getContactName());
                        contactMap.put("contact_Image", uri.toString());
                        contactMap.put("contact_Phone",user.getContactPhone());
                        contactMap.put("contact_Email",user.getContactEmail());

                        firebaseFirestore.collection("ContactList").document(currentUser).collection("User")
                        .document(user.getContactId()).set(contactMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                insertResultLiveData.setValue("Insert Successfully");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                insertResultLiveData.setValue(e.toString());
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                insertResultLiveData.setValue(e.toString());
            }
        });

        return insertResultLiveData;
    }

    public MutableLiveData<List<ContactUser>> getDataFromFireStore(){
        MutableLiveData<List<ContactUser>> getFireStoreMutableLiveData = new MutableLiveData<>();
        String currentUser = firebaseAuth.getCurrentUser().getUid();
        List<ContactUser>contactList = new ArrayList<>();

        firebaseFirestore.collection("ContactList").document(currentUser).collection("User")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                contactList.clear();
                for(DocumentSnapshot documentSnapshot:task.getResult()){
                    String id = documentSnapshot.getString("contact_Id");
                    String name = documentSnapshot.getString("contact_Name");
                    String image = documentSnapshot.getString("contact_Image");
                    String phone = documentSnapshot.getString("contact_Phone");
                    String email = documentSnapshot.getString("contact_Email");

                    ContactUser user = new ContactUser(id,name,image,phone,email);
                    contactList.add(user);
                }
                getFireStoreMutableLiveData.setValue(contactList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        return getFireStoreMutableLiveData;
    }

    public void deleteDataFirebase(String id){
        String currentUser = firebaseAuth.getCurrentUser().getUid();
        StorageReference deleteImage = storageReference.child("profile_image").child(currentUser).child(id+"jpg");
        deleteImage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                firebaseFirestore.collection("ContactList").document(currentUser)
                .collection("User").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
            }
        });
    }
}
