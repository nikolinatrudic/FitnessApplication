package com.example.fitnessapplication.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fitnessapplication.database.entities.User;

@Dao
public interface UserDao {

    @Insert
    void insertUser(User user);

    @Query("SELECT * from user u WHERE u.username like :username1")
    User getUser(String username1);

    @Query("SELECT * FROM user u WHERE u.id like :idU")
    User getUserById(int idU);

    @Query("UPDATE user SET username = :newUserName WHERE id = :idUser")
    void updateUsername(long idUser, String newUserName);

    @Query("UPDATE user SET email = :newUserEmail WHERE id = :idUser")
    void updateEmail(long idUser, String newUserEmail);

    @Query("UPDATE user SET password = :newUserPassword WHERE id = :idUser")
    void updatePassword(long idUser, String newUserPassword);

    @Query("UPDATE user SET height = :newUserHeight WHERE id = :idUser")
    void updateHeight(long idUser, Integer newUserHeight);

    @Query("UPDATE user SET weight = :newUserWeight WHERE id = :idUser")
    void updateWeight(long idUser, Integer newUserWeight);


}
