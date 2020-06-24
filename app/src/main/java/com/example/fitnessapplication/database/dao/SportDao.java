package com.example.fitnessapplication.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fitnessapplication.database.entities.Sport;

import java.util.List;

@Dao
public interface SportDao {

    @Insert
    void insertSport(Sport sport);

    @Query("SELECT * from sport s WHERE s.name like :name1")
    Sport findSport(String name1);
    @Query("SELECT * from sport s WHERE s.sportId like :id")
    Sport findSportId(int id);

    @Query("SELECT * from sport s")
    List<Sport> getSports();

    @Query("UPDATE sport SET forumId= :forumId WHERE sportId = :idSport")
    void updateForum(long idSport, Integer forumId);

    @Query("UPDATE sport SET calPerKm= :calPerKm WHERE sportId = :idSport")
    void updateWeight(long idSport, Integer calPerKm);
}
