package com.example.mvvm_firestore.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mvvm_firestore.model.ContactUser;
import com.example.mvvm_firestore.repository.ContactRepository;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {
    private ContactRepository repository;
    public LiveData<String> insertResultLiveData;
    public LiveData<List<ContactUser>> getContactLiveData;
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
}
