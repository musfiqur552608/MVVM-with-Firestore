package com.example.mvvm_firestore.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvvm_firestore.R;
import com.example.mvvm_firestore.adapter.ContactAdapter;
import com.example.mvvm_firestore.dialog.DetailsDialog;
import com.example.mvvm_firestore.model.ContactUser;
import com.example.mvvm_firestore.model.UpdateUser;
import com.example.mvvm_firestore.viewmodel.ContactViewModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class ListFragment extends Fragment implements ContactAdapter.ClickInterface{
    private ContactViewModel contactViewModel;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private List<ContactUser> userList = new ArrayList<>();
    private ContactAdapter adapter;
    private int userPosition;

    private Button updateImgBtn, updateInfoBtn;
    private TextView idText;
    private EditText nameEdit, phoneEdit, emailEdit;

    private Uri updateUri = null;


    public ListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        setUpRecycle();
        searchView = view.findViewById(R.id.schViewId);
        recyclerView = view.findViewById(R.id.recylerViewId);
        adapter = new ContactAdapter(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                AlertDialog dialog = new SpotsDialog.Builder().setContext(getActivity()).setTheme(R.style.Custom).setCancelable(true).build();
                dialog.show();
                contactViewModel.search(query);
                contactViewModel.searchLiveData.observe(getViewLifecycleOwner(), new Observer<List<ContactUser>>() {
                    @Override
                    public void onChanged(List<ContactUser> contactuserList) {
                        dialog.dismiss();
                        userList = contactuserList;
                        adapter.getContactList(contactuserList);
                        recyclerView.setAdapter(adapter);
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void setUpRecycle() {
        AlertDialog dialog = new SpotsDialog.Builder().setContext(getActivity()).setTheme(R.style.Custom).setCancelable(true).build();
        dialog.show();
        contactViewModel.show();
        contactViewModel.getContactLiveData.observe(getViewLifecycleOwner(), new Observer<List<ContactUser>>() {
            @Override
            public void onChanged(List<ContactUser> contactUsers) {
                dialog.dismiss();
                userList = contactUsers;
                adapter.getContactList(userList);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private void initViewModel() {
        contactViewModel = new ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(ContactViewModel.class);
    }

    @Override
    public void onItemClick(int position) {
        openDetailsDialog(position);
    }

    private void openDetailsDialog(int position) {
        DetailsDialog dialog = new DetailsDialog(userList,position);
        dialog.show(getChildFragmentManager(),"Details Dialog");

    }

    @Override
    public void onLongItemClick(int position) {
        String id = userList.get(position).getContactId();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] option = {"Update", "Delete"};
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    update(position);
                }
                if(i == 1){
                    contactViewModel.delete(id);
                    Toast.makeText(getActivity(), "Delete Successfully...!!!", Toast.LENGTH_SHORT).show();
                    userList.remove(position);
                    adapter.notifyItemRemoved(position);
                }
            }
        }).create().show();
    }

    private void update(int position) {
        userPosition = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.update_dialog,null);
        builder.setView(view).setTitle("Update Contact").setIcon(R.drawable.ic_update).setCancelable(true)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        updateImgBtn = view.findViewById(R.id.updateImgBtn);
        updateInfoBtn = view.findViewById(R.id.updateInfoBtn);
        idText = view.findViewById(R.id.updateID);
        nameEdit = view.findViewById(R.id.updateName);
        phoneEdit = view.findViewById(R.id.updatePhone);
        emailEdit = view.findViewById(R.id.updateEmail);

        updateInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = userList.get(position).getContactId();
                String name = nameEdit.getText().toString();
                String phone = phoneEdit.getText().toString();
                String email = emailEdit.getText().toString();
                UpdateUser user = new UpdateUser(id,name,phone,email);
                contactViewModel.updateInfo(user);
                Toast.makeText(getActivity(), "Info Updated...!!!", Toast.LENGTH_SHORT).show();
            }
        });
        updateImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });
        idText.setText("ID: "+userList.get(position).getContactId());
        nameEdit.setText(userList.get(position).getContactName());
        phoneEdit.setText(userList.get(position).getContactPhone());
        emailEdit.setText(userList.get(position).getContactEmail());
        builder.create().show();
    }

    private void pickImage() {
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
                updateUri = result.getUri();
                String id = userList.get(userPosition).getContactId();
                contactViewModel.updateImage(id,updateUri);
                Toast.makeText(getActivity(), "Image Updated...!!!", Toast.LENGTH_SHORT).show();
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


}