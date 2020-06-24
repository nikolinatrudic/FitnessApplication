package com.example.fitnessapplication.activity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fitnessapplication.menu.DrawerLocker;
import com.example.fitnessapplication.R;
import com.example.fitnessapplication.database.FitnessDatabase;
import com.example.fitnessapplication.fragment.ProfileFragment;
import com.example.fitnessapplication.fragment.StartPageFragment;
import com.example.fitnessapplication.fragment.StepCounterFragment;
import com.example.fitnessapplication.database.LoggedInUser;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLocker {
    FrameLayout frameLayout;
    private StartPageFragment spf;
    private DrawerLayout drawerLayout;
    private NavigationView nav;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayout = (FrameLayout) findViewById(R.id.fragment_container);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav = findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(this);

        drawerLayout = findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.purple));

        if(LoggedInUser.getInstance().getUser() != null || !getPreferences(MODE_PRIVATE).getString("logged_in_user_username","").equals("")) {
            //ovim cekiramo da li je korisnik registrovan
            if(!getPreferences(MODE_PRIVATE).getString("logged_in_user_username","").equals("")){
                LoggedInUser.getInstance().setUser(FitnessDatabase.getInstance(getApplicationContext()).userDao().getUser(getPreferences(MODE_PRIVATE).getString("logged_in_user_username","")));
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            StepCounterFragment stepCounterFragment = new StepCounterFragment();
            fragmentTransaction.add(R.id.fragment_container, stepCounterFragment);
            fragmentTransaction.commit();
        }else {
            spf = new StartPageFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.add(R.id.fragment_container, spf);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //ovo sluzi kako bi se navigation na pocetnoj strani disable-ovao
    public void setDrawerEnabled(boolean enabled) {
        int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED :
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        drawerLayout.setDrawerLockMode(lockMode);
        toggle.setDrawerIndicatorEnabled(enabled);
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

    //ovde se obradjuju akcije iz menija
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.home:
                home();
                break;
            case R.id.profile:
                profile();
                break;
            case R.id.forum:
                //prebaci na forum
                break;
            case R.id.log_out:
                logOut();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void home() {
        if(!getPreferences(MODE_PRIVATE).getString("logged_in_user_username","").equals("")){
            LoggedInUser.getInstance().setUser(FitnessDatabase.getInstance(getApplicationContext()).userDao().getUser(getPreferences(MODE_PRIVATE).getString("logged_in_user_username","")));
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        StepCounterFragment stepCounterFragment = new StepCounterFragment.Builder().setType("user").build();
        fragmentTransaction.replace(R.id.fragment_container, stepCounterFragment);
        fragmentTransaction.commit();
    }

    private void logOut(){
        LoggedInUser.getInstance().setUser(null);

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("logged_in_user_username", null);
        editor.apply();

        //nav.setVisibility(View.GONE);
        setDrawerEnabled(false);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        StartPageFragment startPageFragment = new StartPageFragment();
        fragmentTransaction.replace(R.id.fragment_container, startPageFragment);
        fragmentTransaction.commit();
    }

    private void profile(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ProfileFragment profileFragment = new ProfileFragment();
        fragmentTransaction.replace(R.id.fragment_container, profileFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}