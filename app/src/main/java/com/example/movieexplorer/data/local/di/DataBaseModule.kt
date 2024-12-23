package com.example.movieexplorer.data.local.di

import android.content.Context
import androidx.room.Room
import com.example.movieexplorer.data.local.dao.MovieDao
import com.example.movieexplorer.data.local.database.MovieDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): MovieDataBase{
        return Room.databaseBuilder(
            context= context,
            MovieDataBase::class.java,
            "movie_database"
        ).build()
    }
    @Provides
    fun provideMovieDao(dataBase: MovieDataBase): MovieDao {
        return dataBase.movieDao()
    }

}