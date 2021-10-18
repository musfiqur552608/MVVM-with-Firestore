package com.example.mvvm_firestore.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mvvm_firestore.model.SignInUser;
import com.example.mvvm_firestore.repository.SignInRepository;
import com.google.firebase.auth.AuthCredential;


public class SignInViewModel extends AndroidViewModel {
    private SignInRepository repository;
    public LiveData<SignInUser> checkAuthenticateLiveData;
    public LiveData<String> authenticationLiveData;
    public LiveData<SignInUser> collectUserInfoLiveData;
    public SignInViewModel(@NonNull Application application) {
        super(application);
        repository = new SignInRepository();
    }

    public void signInWithGoogle(AuthCredential authCredential){
        authenticationLiveData = repository.firebaseSignInWithGoogle(authCredential);
    }

    public void checkAuthenticate(){
        checkAuthenticateLiveData = repository.checkAuthenticationInFirebase();
    }

    public void collectUserInfo(){
        collectUserInfoLiveData = repository.collectUserData();
    }
}
