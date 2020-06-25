package com.example.fitnessapplication;

public class WorkoutFactory {

    public WorkoutInterface getWorkout(String workoutName, int steps, int caloriesPerKm){
        if(workoutName == null){
            return null;
        }
        if(workoutName.equalsIgnoreCase("walking")){
            return new WalkWorkout(steps, caloriesPerKm);
        } else if(workoutName.equalsIgnoreCase("run")){
            return new RunningWorkout(steps, caloriesPerKm);
        }

        return null;
    }
}
