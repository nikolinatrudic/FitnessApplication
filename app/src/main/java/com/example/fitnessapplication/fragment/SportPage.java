package com.example.fitnessapplication.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnessapplication.R;
import com.example.fitnessapplication.database.FitnessDatabase;
import com.example.fitnessapplication.database.dao.ForumDao;
import com.example.fitnessapplication.database.entities.Forum;
import com.example.fitnessapplication.database.entities.Sport;

import org.w3c.dom.Text;

import java.text.DateFormat;


public class SportPage extends Fragment {
    private TextView sportName;
    private TextView kmTxt;
    private TextView caloriesTxt;
    private TextView avgSpeedTxt;

    private Button startWorkoutBtn;
    private Button forumBtn;

    private Sport sport;
    private Forum forum;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 44;

    public SportPage() {
        // Required empty public constructor
    }
    public void setSport(Sport sport){
        if(sport!=null) {
            this.sport = sport;
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sport_page, container, false);

        forumBtn = (Button) view.findViewById(R.id.forumBtn);
        startWorkoutBtn = (Button) view.findViewById(R.id.startWorkoutBtn);

        sportName = (TextView) view.findViewById(R.id.sportTxt);
        kmTxt = (TextView) view.findViewById(R.id.kmText);
        caloriesTxt= (TextView) view.findViewById(R.id.caloriesText);
        avgSpeedTxt = (TextView) view.findViewById(R.id.avgSpeedTxt);

        sportName.setText(sport.getName());
        //todo: get from the database all the data
        kmTxt.setText("0");
        //caloriesTxt.setText("0");
        avgSpeedTxt.setText("0");
        caloriesTxt.setText(sport.getCaloriesPerKm()+" ");

        startWorkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

                    return;
                }
               openMaps();
            }
        });

        forumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                ForumFragment forumFragment = new ForumFragment();
                forumFragment.setSport(sport);

                ForumDao forumDao = FitnessDatabase.getInstance(getContext()).forumDao();
                forum = forumDao.findForum(sport.getName()+"");
                Log.e("MSf", forum.getName()+"");
                forumFragment.setForum(forum);
                fragmentTransaction.replace(R.id.fragment_container, forumFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }
    private void openMaps(){
        MapsFragment mapsFragment = new MapsFragment();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, mapsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openMaps();
            } else {
                Toast.makeText(getActivity(),
                        "Location Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}