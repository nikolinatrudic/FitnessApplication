package com.example.fitnessapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fitnessapplication.R;
import com.example.fitnessapplication.database.FitnessDatabase;
import com.example.fitnessapplication.database.entities.User;
import com.example.fitnessapplication.database.LoggedInUser;

public class EditProfileFragment extends Fragment {

    private Button buttonUpdate;
    private EditText editTextEmail;
    private EditText editTextHeight;
    private EditText editTextWeight;
    private EditText editTextPassword;
    private User user;

    private FitnessDatabase fdb;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);


        fdb = FitnessDatabase.getInstance(getContext());

        editTextEmail = view.findViewById(R.id.plainTextEmail);
        editTextHeight = view.findViewById(R.id.plainTextHeight);
        editTextWeight = view.findViewById(R.id.plainTextWeight);
        editTextPassword = view.findViewById(R.id.plainTextPassword);

        if (LoggedInUser.getInstance().getUser() != null) {
            user = LoggedInUser.getInstance().getUser();

            editTextEmail.setText(user.getEmail());
            editTextHeight.setText(user.getHeight() + "");
            editTextWeight.setText(user.getWeight() + "");
            editTextPassword.setText(user.getPassword());
        }

        buttonUpdate = view.findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!editTextEmail.getText().toString().equalsIgnoreCase(user.getEmail()) && !editTextEmail.getText().toString().equalsIgnoreCase("") && editTextEmail.getText().toString() != null) {
                    fdb.userDao().updateEmail((long) user.getId(), editTextEmail.getText().toString());
                    Toast.makeText(getContext(), "Email is successfully updated!", Toast.LENGTH_SHORT).show();
                }
                if (!editTextWeight.getText().toString().equalsIgnoreCase(user.getWeight() + "") && !editTextWeight.getText().toString().equalsIgnoreCase("") && editTextWeight.getText().toString() != null) {
                    fdb.userDao().updateWeight((long) user.getId(), Integer.parseInt(editTextWeight.getText().toString()));
                    Toast.makeText(getContext(), "Weight is successfully updated!", Toast.LENGTH_SHORT).show();
                }

                if (!editTextHeight.getText().toString().equalsIgnoreCase(user.getHeight() + "") && !editTextHeight.getText().toString().equalsIgnoreCase("") && editTextHeight.getText().toString() != null) {
                    fdb.userDao().updateHeight((long) user.getId(), Integer.parseInt(editTextHeight.getText().toString()));
                    Toast.makeText(getContext(), "Height is successfully updated!", Toast.LENGTH_SHORT).show();
                }

                if (!editTextPassword.getText().toString().equalsIgnoreCase(user.getPassword()) && !editTextPassword.getText().toString().equalsIgnoreCase("") && editTextPassword.getText().toString() != null) {
                    fdb.userDao().updatePassword((long) user.getId(), editTextPassword.getText().toString());
                    Toast.makeText(getContext(), "Password is successfully updated!", Toast.LENGTH_SHORT).show();
                }



            }

        });

        return view;
    }
}
