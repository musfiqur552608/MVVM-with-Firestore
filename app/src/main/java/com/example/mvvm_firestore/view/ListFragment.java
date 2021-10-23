package com.example.mvvm_firestore.view;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mvvm_firestore.R;
import com.example.mvvm_firestore.adapter.ContactAdapter;
import com.example.mvvm_firestore.dialog.DetailsDialog;
import com.example.mvvm_firestore.model.ContactUser;
import com.example.mvvm_firestore.viewmodel.ContactViewModel;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class ListFragment extends Fragment implements ContactAdapter.ClickInterface{
    private ContactViewModel contactViewModel;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private List<ContactUser> userList = new ArrayList<>();
    private ContactAdapter adapter;


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
        Toast.makeText(getActivity(), ""+position+"Long", Toast.LENGTH_SHORT).show();
    }
}