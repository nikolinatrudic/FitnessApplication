package com.example.fitnessapplication.database;

import com.example.fitnessapplication.database.entities.User;

public class UserFactory {

    public static User getUser(String username, String email, String password, int height, int width, String gender, String admin){
        User user = new User(username, email, password, height, width, gender, admin);

        return  user;
    }
}
