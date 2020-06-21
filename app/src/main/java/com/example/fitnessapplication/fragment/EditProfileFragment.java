package com.example.fitnessapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fitnessapplication.R;
import com.example.fitnessapplication.database.entities.User;
import com.example.fitnessapplication.singleton.LoggedInUser;

public class EditProfileFragment extends Fragment {

    private Button buttonUpdate;
    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextHeight;
    private EditText editTextWeight;
    private EditText editTextPassword;
    private User user;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        editTextUsername = view.findViewById(R.id.plainTextUsername);
        editTextEmail = view.findViewById(R.id.plainTextEmail);
        editTextHeight = view.findViewById(R.id.plainTextHeight);
        editTextWeight = view.findViewById(R.id.plainTextWeight);
        editTextPassword = view.findViewById(R.id.plainTextPassword);

        if (LoggedInUser.getInstance().getUser() != null) {
            user = LoggedInUser.getInstance().getUser();

            editTextUsername.setText(user.getUsername());
            editTextEmail.setText(user.getEmail());
            editTextHeight.setText(user.getHeight() + "");
            editTextWeight.setText(user.getWeight() + "");
            editTextPassword.setText(user.getPassword());
        }

        buttonUpdate = view.findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });

        return view;
    }
}
