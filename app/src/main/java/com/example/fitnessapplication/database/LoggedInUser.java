package com.example.fitnessapplication.database;

import com.example.fitnessapplication.database.entities.User;

public class LoggedInUser {

    private static LoggedInUser instance = new LoggedInUser();
    User user;

    private LoggedInUser(){}

    public static LoggedInUser getInstance() {
        return instance;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
