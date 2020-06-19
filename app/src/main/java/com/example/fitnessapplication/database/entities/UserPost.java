package com.example.fitnessapp.database.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserPost {
    @Embedded
    public User user;

    @Relation(parentColumn = "id", entityColumn = "userId", entity = Post.class)
    public List<Post> posts;
}
