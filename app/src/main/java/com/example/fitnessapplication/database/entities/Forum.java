package com.example.fitnessapplication.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "forum")
public class Forum {
    @PrimaryKey(autoGenerate = true)
    private int forumId;

    @ColumnInfo(name = "name")
    private String name;

    public Forum(int forumId, String name) {
        this.forumId = forumId;
        this.name = name;
    }

    public int getForumId() {
        return forumId;
    }

    public void setForumId(int forumId) {
        this.forumId = forumId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
