package com.example.fitnessapplication.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fitnessapplication.R;
import com.example.fitnessapplication.database.FitnessDatabase;
import com.example.fitnessapplication.database.entities.User;
import com.example.fitnessapplication.factory.UserFactory;
import com.example.fitnessapplication.singleton.LoggedInUser;

public class SignupFragment extends Fragment {

    private EditText username;
    private EditText email;
    private EditText password;
    private EditText height;
    private EditText weight;
    private Spinner gender;
    private Button createAccount;

    private FitnessDatabase fdb;

    public SignupFragment() {
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
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        createAccount = (Button) view.findViewById(R.id.button);
        username = (EditText) view.findViewById(R.id.username1);
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                User user1 = fdb.userDao().getUser(username.getText().toString());
                if(user1 != null){
                    username.setTextColor(Color.RED);
                    createAccount.setEnabled(false);
                }else{
                    username.setTextColor(Color.BLACK);
                    createAccount.setEnabled(true);
                }
            }
        });
        email = (EditText) view.findViewById(R.id.email);
        password = (EditText) view.findViewById(R.id.password1);
        height = (EditText) view.findViewById(R.id.height);
        weight = (EditText) view.findViewById(R.id.weight);
        gender = (Spinner) view.findViewById(R.id.genderSpinner);

        fdb = FitnessDatabase.getInstance(getContext());

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("") || email.getText().toString().equals("") || password.getText().toString().equals("") || height.getText().toString().equals("") || weight.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "You need to fill all fields!",Toast.LENGTH_SHORT).show();
                } else {
                    String username1 = username.getText().toString();
                    String email1 = email.getText().toString();
                    String password1 = password.getText().toString();
                    int height1 = Integer.parseInt(height.getText().toString());
                    int weight1 = Integer.parseInt(weight.getText().toString());
                    String gender1 = gender.getSelectedItem().toString();

                    User user = UserFactory.getUser(username1, email1, password1, height1, weight1, gender1);
                    fdb.userDao().insertUser(user);

                    LoggedInUser.getInstance().setUser(user); // ovo je singleton pattern, znaci imamo samo jednog ulogovanog korisnika

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    StepCounterFragment usp = new StepCounterFragment();
                    fragmentTransaction.replace(R.id.frameLayout, usp);
                    fragmentTransaction.commit();
                }
            }
        });


        return view;
    }
}