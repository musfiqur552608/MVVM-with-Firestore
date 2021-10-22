package com.example.mvvm_firestore.repository;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.mvvm_firestore.model.ContactUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
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
}
