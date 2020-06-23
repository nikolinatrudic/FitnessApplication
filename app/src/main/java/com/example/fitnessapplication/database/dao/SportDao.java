package com.example.fitnessapplication.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fitnessapplication.database.entities.Sport;

@Dao
public interface SportDao {

    @Insert
    void insertSport(Sport sport);

    @Query("SELECT * from sport s WHERE s.name like :name1")
    Sport getSport(String name1);

    @Query("UPDATE sport SET forumId= :forumId WHERE sportId = :idSport")
    void updateForum(long idSport, Integer forumId);

    @Query("UPDATE sport SET calPerKm= :calPerKm WHERE sportId = :idSport")
    void updateWeight(long idSport, Integer calPerKm);
}
