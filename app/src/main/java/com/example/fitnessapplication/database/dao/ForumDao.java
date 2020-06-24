package com.example.fitnessapplication.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.fitnessapplication.database.entities.Forum;

@Dao
public interface ForumDao {

    @Insert
    void insertForum(Forum forum);
}
