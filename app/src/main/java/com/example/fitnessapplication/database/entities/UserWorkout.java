package com.example.fitnessapplication.database.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.fitnessapplication.database.entities.User;

import java.util.List;

public class UserWorkout {
    @Embedded
    public User user;

    @Relation(parentColumn = "id", entityColumn = "userId", entity = Workout.class)
    public List<Workout> workouts;
}
