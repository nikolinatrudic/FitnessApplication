package com.example.fitnessapplication.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fitnessapplication.database.entities.Sport;
import com.example.fitnessapplication.database.entities.User;
import com.example.fitnessapplication.database.entities.Workout;

import java.util.List;

@Dao
public interface WorkoutDao {
    @Insert
    void insertWorkout(Workout workout);

   @Query("SELECT * from workout w WHERE w.sportId like :sportId1")
   Workout findWorkoutBySportId(int sportId1);

    @Query("SELECT * from workout w WHERE w.userId like :userId1")
    List<Workout> findWorkoutByUserId(int userId1);
    @Query("SELECT * from workout w")
    List<Workout> findWorkouts();
}
