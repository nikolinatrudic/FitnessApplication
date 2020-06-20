package com.example.fitnessapplication.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "workout")
public class Workout {

    @PrimaryKey(autoGenerate = true)
    private int workoutId;

    @ColumnInfo(name = "km")
    private float km;

    private int userId;

    private int sportId;
}
