package com.example.fitnessapplication.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.fitnessapplication.database.entities.Post;

@Dao
public interface PostDao {

    @Insert
    void insertPost(Post post);
}
