package com.example.fitnessapplication;

import com.example.fitnessapplication.database.LoggedInUser;
import com.example.fitnessapplication.database.entities.User;

public class AccelerometerSingleton {

    private static AccelerometerSingleton instance = new AccelerometerSingleton();
    Accelerometer accelerometer;

    private AccelerometerSingleton(){}

    public static AccelerometerSingleton getInstance() {
        return instance;
    }

    public Accelerometer getAccelerometer() {
        return accelerometer;
    }

    public void setAccelerometer(Accelerometer accelerometer) {
        this.accelerometer = accelerometer;
    }
}
