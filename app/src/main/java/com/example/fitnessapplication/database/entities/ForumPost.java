package com.example.fitnessapplication.database.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ForumPost {
    @Embedded
    public Forum forum;

    @Relation(parentColumn = "forumId", entityColumn = "forumId", entity = Post.class)
    public List<Post> posts;
}
