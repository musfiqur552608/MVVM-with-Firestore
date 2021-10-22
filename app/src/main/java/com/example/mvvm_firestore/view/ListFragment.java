package com.example.mvvm_firestore.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mvvm_firestore.R;
import com.example.mvvm_firestore.model.ContactUser;
import com.example.mvvm_firestore.viewmodel.ContactViewModel;

import java.util.List;


public class ListFragment extends Fragment {
    private ContactViewModel contactViewModel;


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
    }

    private void setUpRecycle() {
        contactViewModel.show();
        contactViewModel.getContactLiveData.observe(getViewLifecycleOwner(), new Observer<List<ContactUser>>() {
            @Override
            public void onChanged(List<ContactUser> contactUsers) {
                String name = contactUsers.get(0).getContactName();
                Toast.makeText(getActivity(), ""+name, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViewModel() {
        contactViewModel = new ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(ContactViewModel.class);
    }
}