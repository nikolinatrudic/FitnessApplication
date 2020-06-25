package com.example.fitnessapplication.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fitnessapplication.database.entities.Post;

import java.util.List;

@Dao
public interface PostDao {

    @Insert
    void insertPost(Post post);

    @Query("SELECT * from post")
    List<Post> getAllPosts();

    @Query("SELECT * from post p where p.forumId like :forumId1")
    List<Post> getForumPosts(int forumId1);
}
