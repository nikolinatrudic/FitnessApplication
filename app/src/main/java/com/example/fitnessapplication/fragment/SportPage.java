package com.example.fitnessapplication.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fitnessapplication.R;

import org.w3c.dom.Text;

import java.text.DateFormat;


public class SportPage extends Fragment {
    private TextView sportName;
    private TextView kmTxt;
    private TextView caloriesTxt;
    private TextView avgSpeedTxt;

    private Button startWorkoutBtn;
    private Button forumBtn;

    private String sport;
    public SportPage() {
        // Required empty public constructor
    }
    public void setSport(String sport){
        this.sport = sport;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sport_page, container, false);
        //todo: this buttons need on click listener
        forumBtn = (Button) view.findViewById(R.id.forumBtn);
        startWorkoutBtn = (Button) view.findViewById(R.id.startWorkoutBtn);

        sportName = (TextView) view.findViewById(R.id.sportTxt);
        kmTxt = (TextView) view.findViewById(R.id.kmText);
        caloriesTxt= (TextView) view.findViewById(R.id.caloriesText);
        avgSpeedTxt = (TextView) view.findViewById(R.id.avgSpeedTxt);

        sportName.setText(sport);
        //todo: get from the database all the data
        kmTxt.setText("0");
        caloriesTxt.setText("0");
        avgSpeedTxt.setText("0");

        startWorkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MapsFragment mapsFragment = new MapsFragment();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.fragment_container, mapsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });

        return view;
    }
}