package com.example.mvvm_firestore.view;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mvvm_firestore.R;
import com.example.mvvm_firestore.model.ContactUser;
import com.example.mvvm_firestore.viewmodel.ContactViewModel;
import com.example.mvvm_firestore.viewmodel.SignInViewModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;


public class InsertFragment extends Fragment {

    private EditText insertName, insertNumber, insertEmail;
    private Button saveBtn;
    private CircleImageView insertImage;

    private Uri insertImageUri = null;
    private ContactViewModel contactViewModel;


    public InsertFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insert, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViewModel();

        insertName = view.findViewById(R.id.insertName);
        insertNumber = view.findViewById(R.id.insertPhone);
        insertEmail = view.findViewById(R.id.insertEmail);
        insertImage = view.findViewById(R.id.insertImg);
        saveBtn = view.findViewById(R.id.saveBtn);

        insertImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = randomDigit();
                String name = insertName.getText().toString();
                String phone = insertNumber.getText().toString();
                String email = insertEmail.getText().toString();

                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(email) && insertImageUri != null){
                    AlertDialog dialog = new SpotsDialog.Builder().setContext(getActivity()).setTheme(R.style.Custom).setCancelable(true).build();
                    dialog.show();

                    ContactUser user = new ContactUser(id,name,"image_url",phone,email);
                    contactViewModel.insert(user,insertImageUri);
                    contactViewModel.insertResultLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), ""+s, Toast.LENGTH_SHORT).show();
                        }
                    });
                    insertImage.setImageResource(R.drawable.splash);
                    insertName.setText("");
                    insertNumber.setText("");
                    insertEmail.setText("");

                }
                else {
                    Toast.makeText(getActivity(), "Please fill the all Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initViewModel() {
        contactViewModel = new ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(ContactViewModel.class);
    }

    private void uploadImage() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            else{
                imagePick();
            }
        }
        else{
            imagePick();
        }
    }

    private void imagePick() {
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(getContext(),this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == getActivity().RESULT_OK){
                insertImageUri = result.getUri();
                insertImage.setImageURI(insertImageUri);
            }
            else if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception exception = result.getError();
                Toast.makeText(getActivity(), exception.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getActivity(), "Image Passing Error .....", Toast.LENGTH_SHORT).show();
        }
    }
    private String randomDigit(){
        char[] chars = "1234567890".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for(int i=0;i<4;i++){
            char c = chars[random.nextInt(chars.length)];
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }
}