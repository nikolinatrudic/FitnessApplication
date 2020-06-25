package com.example.fitnessapplication;

public class Accelerometer {
    private int stepsNumber;
    private float[] gravity = new float[3];
    private float[] linear_acceleration = new float[3];
    private double magnitudePrevious;

    public Accelerometer(){
        this.stepsNumber = 0;
    }

    public int calculateSteps(float x, float y, float z){
        final float alpha = (float) 0.8;

        gravity[0] = alpha * gravity[0] + (1 - alpha) * x;
        gravity[1] = alpha * gravity[1] + (1 - alpha) * y;
        gravity[2] = alpha * gravity[2] + (1 - alpha) * z;

        linear_acceleration[0] = x - gravity[0];
        linear_acceleration[1] = y - gravity[1];
        linear_acceleration[2] = z - gravity[2];


        double magnitude = Math.sqrt(linear_acceleration[0] * linear_acceleration[0] + linear_acceleration[1] * linear_acceleration[1] + linear_acceleration[2] * linear_acceleration[2]);
        double magnitudeDelta = magnitude - magnitudePrevious;
        magnitudePrevious = magnitude;

        if (magnitudeDelta > 6) {
            stepsNumber++;
        }

        return stepsNumber;
    }

    public int getStepsNumber(){
        return stepsNumber;
    }

    public void setStepsNumber(int steps){
        stepsNumber = steps;
    }
}
