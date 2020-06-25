package com.example.fitnessapplication;

import com.example.fitnessapplication.database.LoggedInUser;
import com.example.fitnessapplication.fragment.StepCounterFragment;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class RunningWorkout implements WorkoutInterface {

    private float km;

    public RunningWorkout(float km) {
        this.km = km;
    }

    @Override
    public int countCalories() {
        int calories = (int) ((int) km * 0.07);
        return 0;
    }

    /*@Override
    public float countKm() {
        int userHeight = LoggedInUser.getInstance().getUser().getHeight();

        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        float km = (float) 0.0;
        if(userHeight <= 160){
            km = (float) StepCounterFragment.stepCount / (float) 1320;
        } else if (userHeight > 160 && userHeight <= 180){
            km = (float) StepCounterFragment.stepCount / (float) 1420;
        } else if (userHeight > 180){
            km = (float) StepCounterFragment.stepCount / (float) 1520;
        }

        String stringKm = df.format(km);
        km = Float.parseFloat(stringKm);

        return km;
    }*/

    @Override
    public int calculateSpeed() {
        return 27;
    }
}
