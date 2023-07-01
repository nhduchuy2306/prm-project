package com.example.gearmobile.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.gearmobile.dto.CartDto;

@Database(entities = {CartDto.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CartDao cartDao();
}
