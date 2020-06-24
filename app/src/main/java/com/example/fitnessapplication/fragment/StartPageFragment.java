package com.example.fitnessapplication.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.fitnessapplication.menu.DrawerLocker;
import com.example.fitnessapplication.R;

public class StartPageFragment extends Fragment {

    private Button loginButton;
    private Button signupButton;
    private ImageView picture;
//todo:initialization of sports in database
    public StartPageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start_page, container, false);
        FrameLayout fl = view.findViewById(R.id.fragment_container);

        /*NavigationView nav = getActivity().findViewById(R.id.nav_view);
        if(nav.getVisibility() != View.GONE){
            nav.setVisibility(View.GONE);
        }*/

        ((DrawerLocker) getActivity()).setDrawerEnabled(false);


        loginButton = (Button) view.findViewById(R.id.loginButton);
        signupButton = (Button) view.findViewById(R.id.signupButton);

        signupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    SignupFragment signupFragment = new SignupFragment();
                    fragmentTransaction.replace(R.id.fragment_container, signupFragment);
                    fragmentTransaction.commit(); }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    LoginFragment loginFragment = new LoginFragment();
                    fragmentTransaction.replace(R.id.fragment_container, loginFragment);
                    fragmentTransaction.commit();
                }
        });

        picture = (ImageView) view.findViewById(R.id.imageViewLogo);
        picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StepCounterFragment startPageFragment = new StepCounterFragment.Builder().setType("guest").build();

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.replace(R.id.fragment_container, startPageFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
        });

        return view;
    }
}