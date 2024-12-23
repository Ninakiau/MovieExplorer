package com.example.movieexplorer.data.local.di

import com.example.movieexplorer.data.remote.mapper.MovieMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {
    @Provides
    fun provideMovieMapper(): MovieMapper {
        return MovieMapper()
    }

}