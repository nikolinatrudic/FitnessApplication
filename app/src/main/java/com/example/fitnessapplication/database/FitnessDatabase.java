package com.example.fitnessapplication.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Database;

import com.example.fitnessapplication.database.dao.ForumDao;
import com.example.fitnessapplication.database.dao.PostDao;
import com.example.fitnessapplication.database.dao.SportDao;
import com.example.fitnessapplication.database.dao.UserDao;
import com.example.fitnessapplication.database.dao.WorkoutDao;
import com.example.fitnessapplication.database.entities.Post;
import com.example.fitnessapplication.database.entities.Sport;
import com.example.fitnessapplication.database.entities.User;
import com.example.fitnessapplication.database.entities.Workout;
import com.example.fitnessapplication.database.entities.Forum;


@Database(entities = {User.class, Workout.class, Sport.class, Forum.class, Post.class}, exportSchema = false, version = 4)
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
    public abstract WorkoutDao workoutDao();
    public abstract SportDao sportDao();
    public abstract UserDao userDao();
    public abstract ForumDao forumDao();
    public abstract PostDao postDao();

}