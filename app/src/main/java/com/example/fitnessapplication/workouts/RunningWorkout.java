package com.example.fitnessapplication.workouts;

import com.example.fitnessapplication.database.LoggedInUser;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class RunningWorkout implements WorkoutInterface {

    private int steps;
    private int caloriesPerKm;

    public RunningWorkout(int steps, int caloriesPerKm) {
        this.steps = steps;
        this.caloriesPerKm = caloriesPerKm;
    }

    @Override
    public float countCalories() {
        float calories = (float) (calculateKm() * caloriesPerKm);
        return calories;
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
        return 27;
    }
}
