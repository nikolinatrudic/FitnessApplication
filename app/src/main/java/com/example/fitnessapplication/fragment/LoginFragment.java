package com.example.fitnessapplication.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fitnessapplication.R;
import com.example.fitnessapplication.database.FitnessDatabase;
import com.example.fitnessapplication.database.entities.User;
import com.example.fitnessapplication.database.LoggedInUser;


public class LoginFragment extends Fragment {

    private EditText username;
    private EditText password;
    private Button loginButton;

    private FitnessDatabase fdb;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        username = (EditText) view.findViewById(R.id.username1);
        password = (EditText) view.findViewById(R.id.password1);

        loginButton = (Button) view.findViewById(R.id.loginButton);

        fdb = FitnessDatabase.getInstance(getContext());

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username1 = username.getText().toString();
                String password1 = password.getText().toString();

                if(username1.equals("")){
                    Toast.makeText(getContext(), "Enter your username first!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password1.equals("")){
                    Toast.makeText(getContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                User user1 = fdb.userDao().getUser(username1);
                if (user1 != null && password1.equals(user1.getPassword())) {
                    LoggedInUser.getInstance().setUser(user1); // ovo je singleton pattern, znaci imamo samo jednog ulogovanog korisnika

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    StepCounterFragment usp = new StepCounterFragment.Builder().setType("user").build();
                    fragmentTransaction.replace(R.id.fragment_container, usp);
                    fragmentTransaction.commit();
                } else {
                    if (user1 == null)
                        Toast.makeText(getContext(), "Username doesn't exist!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getContext(), "Incorrect password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return  view;
    }
}