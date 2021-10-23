package com.example.anandu_sem2_final_project.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {UserData.class},version = 1)
@TypeConverters({ImageTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public  abstract UserDao userDao();

    public static AppDatabase INSTANCE;

    public static AppDatabase getDbInstance(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "UserDatabase")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
