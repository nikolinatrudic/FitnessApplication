package com.example.fitnessapplication.database.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.fitnessapplication.database.entities.User;

import java.util.List;

public class UserPost {
    @Embedded
    public User user;

    @Relation(parentColumn = "id", entityColumn = "userId", entity = Post.class)
    public List<Post> posts;
}
