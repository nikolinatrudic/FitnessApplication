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

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public void setKm(float km) {
        this.km = km;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public float getKm() {
        return km;
    }

    public int getUserId() {
        return userId;
    }

    public int getSportId() {
        return sportId;
    }
}
