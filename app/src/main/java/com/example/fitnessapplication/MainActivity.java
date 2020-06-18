package com.example.fitnessapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.FrameLayout;

import com.example.fitnessapplication.fragment.StartPageFragment;
import com.example.fitnessapplication.singleton.LoggedInUser;

public class MainActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    private StartPageFragment spf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        spf = new StartPageFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.frameLayout, spf);
        fragmentTransaction.commit();
    }
}