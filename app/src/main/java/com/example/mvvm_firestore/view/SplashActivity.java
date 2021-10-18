package com.example.mvvm_firestore.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.example.mvvm_firestore.model.SignInUser;
import com.example.mvvm_firestore.viewmodel.SignInViewModel;

public class SplashActivity extends AppCompatActivity {

    private SignInViewModel signInViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSplashViewModel();
        checkIfUserAuthenticate();
    }

    private void checkIfUserAuthenticate() {
        signInViewModel.checkAuthenticate();
        signInViewModel.checkAuthenticateLiveData.observe(this, new Observer<SignInUser>() {
            @Override
            public void onChanged(SignInUser signInUser) {
                if(!signInUser.isAuth){
                    goToSignActivity();
                }
                else{
                    gotoMainActivity();
                }
            }
        });
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToSignActivity() {
        Intent intent = new Intent(SplashActivity.this,SigninActivity.class);
        startActivity(intent);
        finish();
    }

    private void initSplashViewModel() {
        signInViewModel = new ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SignInViewModel.class);
    }
}