package com.example.fitnessapplication;

public class WorkoutFactory {

    public WorkoutInterface getWorkout(String workoutName, float km){
        if(workoutName == null){
            return null;
        }
        if(workoutName.equalsIgnoreCase("walking")){
            return new WalkWorkout(km);
        } else if(workoutName.equalsIgnoreCase("run")){
            return new RunningWorkout(km);
        }

        return null;
    }
}
