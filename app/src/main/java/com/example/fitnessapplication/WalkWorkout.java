package com.example.fitnessapplication;

import com.example.fitnessapplication.database.LoggedInUser;
import com.example.fitnessapplication.fragment.StepCounterFragment;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class WalkWorkout implements WorkoutInterface {

    private int steps;

    public WalkWorkout(int steps){
        this.steps = steps;
    }

    @Override
    public int countCalories() {
        int calories = (int) ((int) steps * 0.04);
        return 0;
    }

    @Override
    public float calculateKm() {
        int userHeight = LoggedInUser.getInstance().getUser().getHeight();

        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        float km = (float) 0.0;
        if(userHeight <= 160){
            km = (float) steps / (float) 1320;
        } else if (userHeight > 160 && userHeight <= 180){
            km = (float) steps / (float) 1420;
        } else if (userHeight > 180){
            km = (float) steps / (float) 1520;
        }

        String stringKm = df.format(km);
        km = Float.parseFloat(stringKm);

        return km;
    }

    @Override
    public int calculateSpeed() {
        //speed = km / time;
        return 40;
    }
}
