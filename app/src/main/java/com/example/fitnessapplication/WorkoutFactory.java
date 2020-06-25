package com.example.fitnessapplication;

public class WorkoutFactory {

    public WorkoutInterface getWorkout(String workoutName, int steps){
        if(workoutName == null){
            return null;
        }
        if(workoutName.equalsIgnoreCase("walking")){
            return new WalkWorkout(steps);
        } else if(workoutName.equalsIgnoreCase("run")){
            return new RunningWorkout(steps);
        }

        return null;
    }
}
