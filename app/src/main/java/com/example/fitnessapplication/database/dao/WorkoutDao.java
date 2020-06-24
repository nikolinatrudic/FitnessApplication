package com.example.fitnessapplication.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fitnessapplication.database.entities.Sport;
import com.example.fitnessapplication.database.entities.Workout;

@Dao
public interface WorkoutDao {
    @Insert
    void insertWorkout(Workout workout);

   // @Query("SELECT * from workout w WHERE w.name like :name1")
   // Sport getSport(String name1);

    @Query("UPDATE sport SET forumId= :forumId WHERE sportId = :idSport")
    void updateForum(long idSport, Integer forumId);

    @Query("UPDATE sport SET calPerKm= :calPerKm WHERE sportId = :idSport")
    void updateWeight(long idSport, Integer calPerKm);
}
