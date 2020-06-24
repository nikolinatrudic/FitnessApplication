package com.example.fitnessapplication.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sport")
public class Sport {

    @PrimaryKey(autoGenerate = true)
    private int sportId;

    public Sport( String name, int caloriesPerKm) {
        this.name = name;
        this.caloriesPerKm = caloriesPerKm;
    }

    public int getSportId() {
        return sportId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getForumId() {
        return forumId;
    }

    public void setForumId(int forumId) {
        this.forumId = forumId;
    }

    public int getCaloriesPerKm() {
        return caloriesPerKm;
    }

    public void setCaloriesPerKm(int caloriesPerKm) {
        this.caloriesPerKm = caloriesPerKm;
    }

    @ColumnInfo(name = "name")
    private String name;

    private int forumId;
  
    @ColumnInfo(name = "calPerKm")
    private int caloriesPerKm;
}
