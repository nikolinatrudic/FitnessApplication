package com.example.fitnessapplication.imagesproxy;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class ProxyImage implements Image {

    private RealImage realImage;
    private Drawable imagePathMan; // path to the drawable
    private Drawable imagePathWomen;
    private ImageView imageView;
    private String gender;

    public ProxyImage(Drawable pathMan, Drawable pathWomen, String gender, ImageView imageView) {
        this.imagePathMan = pathMan;
        this.imagePathWomen = pathWomen;
        this.gender = gender;
        this.imageView = imageView;
    }

    @Override
    public void display() {
        if (realImage == null) {
            realImage = new RealImage(imagePathMan, imagePathWomen, gender, imageView);
        }
        realImage.display();
    }
}
