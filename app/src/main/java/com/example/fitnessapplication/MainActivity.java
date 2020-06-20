package com.example.fitnessapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.FrameLayout;

import com.example.fitnessapplication.database.FitnessDatabase;
import com.example.fitnessapplication.fragment.StartPageFragment;
import com.example.fitnessapplication.fragment.StepCounterFragment;
import com.example.fitnessapplication.singleton.LoggedInUser;

public class MainActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    private StartPageFragment spf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);

        Log.e("msg", "USER JE: " + (LoggedInUser.getInstance().getUser() != null));
        if(LoggedInUser.getInstance().getUser() != null || !getPreferences(MODE_PRIVATE).getString("logged_in_user_username","").equals("")) {
            if(!getPreferences(MODE_PRIVATE).getString("logged_in_user_username","").equals("")){
                LoggedInUser.getInstance().setUser(FitnessDatabase.getInstance(getApplicationContext()).userDao().getUser(getPreferences(MODE_PRIVATE).getString("logged_in_user_username","")));
            }
            Log.e("msg", "USER JE: sad" + (LoggedInUser.getInstance().getUser() != null));
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            StepCounterFragment stepCounterFragment = new StepCounterFragment();
            fragmentTransaction.add(R.id.frameLayout, stepCounterFragment);
            fragmentTransaction.commit();
        }else {
            spf = new StartPageFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.add(R.id.frameLayout, spf);
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(LoggedInUser.getInstance().getUser() != null) {
            Log.e("msg", "USER JE ovde sacuvan ");
            SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("logged_in_user_username", LoggedInUser.getInstance().getUser().getUsername());
            editor.apply();
        }
    }
}