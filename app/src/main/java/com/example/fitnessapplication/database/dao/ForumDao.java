package com.example.fitnessapplication.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fitnessapplication.database.entities.Forum;
import com.example.fitnessapplication.database.entities.Sport;

import java.util.List;

@Dao
public interface ForumDao {

    @Insert
    void insertForum(Forum forum);

    @Query("SELECT * from forum f WHERE f.name like :name1")
    Forum findForum(String name1);

    @Query("SELECT * from forum f ")
    List<Forum> getForums();
}
