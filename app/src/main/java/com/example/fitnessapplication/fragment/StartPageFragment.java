package com.example.fitnessapplication.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.fitnessapplication.R;
import com.example.fitnessapplication.singleton.LoggedInUser;

public class StartPageFragment extends Fragment {

    private Button loginButton;
    private Button signupButton;
    private ImageView picture;

    public StartPageFragment() {
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
        View view = inflater.inflate(R.layout.fragment_start_page, container, false);
        FrameLayout fl = view.findViewById(R.id.frameLayout);

        if(LoggedInUser.getInstance().getUser() == null) {
            loginButton = (Button) view.findViewById(R.id.loginButton);
            signupButton = (Button) view.findViewById(R.id.signupButton);

            signupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    SignupFragment signupFragment = new SignupFragment();
                    fragmentTransaction.replace(R.id.frameLayout, signupFragment);
                    fragmentTransaction.commit();
                }
            });

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    LoginFragment loginFragment = new LoginFragment();
                    fragmentTransaction.replace(R.id.frameLayout, loginFragment);
                    fragmentTransaction.commit();
                }
            });

            picture = (ImageView) view.findViewById(R.id.imageViewLogo);
            picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ovde da se prebaci na obican step counter
                }
            });
        } else{
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            StepCounterFragment signupFragment = new StepCounterFragment();
            fragmentTransaction.replace(R.id.frameLayout, signupFragment);
            fragmentTransaction.commit();
        }

        return view;
    }
}