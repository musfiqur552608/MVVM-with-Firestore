package com.example.mvvm_firestore.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.mvvm_firestore.R;
import com.example.mvvm_firestore.model.ContactUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailsDialog extends DialogFragment {
    private CircleImageView circleImageView;
    private TextView idText, nameText, phoneText, emailText;
    private List<ContactUser> userList;
    private int position;

    public DetailsDialog(List<ContactUser> userList, int position) {
        this.userList = userList;
        this.position = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.details_dialog,null);
        builder.setView(view).setTitle("Contact Details").setIcon(R.drawable.ic_view).setCancelable(true)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        circleImageView = view.findViewById(R.id.detailsImage);
        idText= view.findViewById(R.id.detailsId);
        nameText = view.findViewById(R.id.detailsName);
        phoneText = view.findViewById(R.id.detailsPhone);
        emailText = view.findViewById(R.id.detailsEmail);

        Glide.with(view.getContext()).load(userList.get(position).getContactImage())
                .centerCrop().placeholder(R.drawable.splash).into(circleImageView);
        idText.setText("ID: "+userList.get(position).getContactId());
        nameText.setText("Name: "+userList.get(position).getContactName());
        phoneText.setText("Phone: "+userList.get(position).getContactPhone());
        emailText.setText("Email: "+userList.get(position).getContactEmail());
        return builder.create();
    }
}
