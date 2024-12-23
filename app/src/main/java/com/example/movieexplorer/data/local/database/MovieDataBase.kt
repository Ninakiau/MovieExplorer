package com.example.movieexplorer.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movieexplorer.data.local.dao.MovieDao
import com.example.movieexplorer.data.local.entities.MovieEntity


@Database(
    entities = [MovieEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDataBase: RoomDatabase(){
    abstract fun movieDao(): MovieDao

}