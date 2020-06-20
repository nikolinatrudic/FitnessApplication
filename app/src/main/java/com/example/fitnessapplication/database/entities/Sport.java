package com.example.fitnessapplication.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sport")
public class Sport {

    @PrimaryKey(autoGenerate = true)
    private int sportId;

    @ColumnInfo(name = "name")
    private String name;

    private int forumId;
  
    @ColumnInfo(name = "calPerKm")
    private int caloriesPerKm;
}
