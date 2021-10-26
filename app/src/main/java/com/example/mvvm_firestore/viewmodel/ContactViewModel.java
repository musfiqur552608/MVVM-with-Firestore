package com.example.mvvm_firestore.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mvvm_firestore.model.ContactUser;
import com.example.mvvm_firestore.model.UpdateUser;
import com.example.mvvm_firestore.repository.ContactRepository;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {
    private ContactRepository repository;
    public LiveData<String> insertResultLiveData;
    public LiveData<List<ContactUser>> getContactLiveData;
    public LiveData<List<ContactUser>> searchLiveData;
    public ContactViewModel(@NonNull Application application) {
        super(application);

        repository = new ContactRepository();
    }

    public void insert(ContactUser user, Uri uri){
        insertResultLiveData = repository.insertContactFirestore(user,uri);
    }

    public void show(){
        getContactLiveData = repository.getDataFromFireStore();
    }

    public void delete(String id){
        repository.deleteDataFirebase(id);
    }

    public void updateImage(String id, Uri uri){
        repository.updateImageFirebase(id,uri);
    }
    public void updateInfo(UpdateUser user){
        repository.updateInfoFirebase(user);
    }

    public void search(String s){
        searchLiveData = repository.searchDataFirebase(s);
    }
}
