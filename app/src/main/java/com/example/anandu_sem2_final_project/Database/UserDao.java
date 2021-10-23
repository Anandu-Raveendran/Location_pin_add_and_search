package com.example.anandu_sem2_final_project.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Query("Select * from UserData")
    List<UserData> getAllData();

    @Insert
    void inserData(UserData...userData);

    @Update
    void update(UserData user);

    @Delete
    void delete(UserData user);
}
