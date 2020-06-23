package com.example.fitnessapplication.imagesproxy;


import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class RealImage implements Image {

    private Drawable imagePathMan; // path to the drawable
    private Drawable imagePathWomen;
    private String gender;
    private ImageView profileImage;

    public RealImage(Drawable pathMan, Drawable pathWomen, String gender, ImageView img) {
        this.imagePathMan = pathMan;
        this.imagePathWomen = pathWomen;
        this.gender = gender;
        this.profileImage = img;
    }


    @Override
    public void display() {
        if (gender.equalsIgnoreCase("male")) {
            profileImage.setImageDrawable(imagePathMan);
        } else if (gender.equalsIgnoreCase("female")) {
            profileImage.setImageDrawable(imagePathWomen);
        }
    }
}
