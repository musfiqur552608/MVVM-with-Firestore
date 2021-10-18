package com.example.mvvm_firestore.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mvvm_firestore.R;
import com.example.mvvm_firestore.model.SignInUser;
import com.example.mvvm_firestore.viewmodel.SignInViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment {


    private Button signOutBtn;
    private CircleImageView proImage;
    private TextView name, email;
    private SignInViewModel signInViewModel;
    private GoogleSignInClient mGoogleSignInClint;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getUserInfo();

        initGoogleSignInClint();

        signOutBtn = view.findViewById(R.id.signoutBtn);
        proImage = view.findViewById(R.id.proImgId);
        name = view.findViewById(R.id.profileName);
        email = view.findViewById(R.id.profileEmail);

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    private void initGoogleSignInClint() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build();
        mGoogleSignInClint = GoogleSignIn.getClient(getActivity(),gso);
    }

    private void signOut() {
        firebaseAuth.signOut();
        mGoogleSignInClint.signOut();
        Intent intent = new Intent(getActivity(),SigninActivity.class);
        startActivity(intent);
        getActivity().onBackPressed();
    }

    private void getUserInfo() {
        signInViewModel = new ViewModelProvider(getActivity(),ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(SignInViewModel.class);
        signInViewModel.collectUserInfo();

        signInViewModel.collectUserInfoLiveData.observe(getViewLifecycleOwner(), new Observer<SignInUser>() {
            @Override
            public void onChanged(SignInUser signInUser) {
                setProfile(signInUser);
            }
        });
    }

    private void setProfile(SignInUser signInUser) {
        if(signInUser != null){
            Glide.with(getActivity()).load(signInUser.getImageUrl()).centerCrop().placeholder(R.drawable.signin).into(proImage);
            name.setText(signInUser.getName());
            email.setText(signInUser.getEmail());
        }
    }
}