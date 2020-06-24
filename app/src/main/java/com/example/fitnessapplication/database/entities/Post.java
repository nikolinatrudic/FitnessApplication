package com.example.fitnessapplication.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "post")
public class Post {
    @PrimaryKey(autoGenerate = true)
    private int postId;

    private int userId;

    private int forumId;

    @ColumnInfo(name = "text")
    private String text;

    @ColumnInfo(name = "heading")
    private String heading;

    public Post(int postId, int userId, int forumId, String text, String heading) {
        this.postId = postId;
        this.userId = userId;
        this.forumId = forumId;
        this.text = text;
        this.heading = heading;
    }
    public Post(){
    }
    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getForumId() {
        return forumId;
    }

    public void setForumId(int forumId) {
        this.forumId = forumId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }
}
