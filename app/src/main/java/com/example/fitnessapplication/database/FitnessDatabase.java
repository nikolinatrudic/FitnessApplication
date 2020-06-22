package com.example.fitnessapplication.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Database;

import com.example.fitnessapplication.database.entities.User;

@Database(entities = {User.class}, exportSchema = false, version = 1)
public abstract class FitnessDatabase extends RoomDatabase {

    private static final String DB_NAME = "fitness_db_test1";
    private static FitnessDatabase instance;

    public static synchronized FitnessDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), FitnessDatabase.class, DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();

        }
        return instance;
    }

    public abstract UserDao userDao();
}