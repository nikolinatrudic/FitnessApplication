package com.example.fitnessapp.database.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserWorkout {
    @Embedded
    public User user;

    @Relation(parentColumn = "id", entityColumn = "userId", entity = Workout.class)
    public List<Workout> workouts;
}
