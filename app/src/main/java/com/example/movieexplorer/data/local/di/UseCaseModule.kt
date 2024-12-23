package com.example.movieexplorer.data.local.di

import com.example.movieexplorer.domain.repository.MovieRepository
import com.example.movieexplorer.domain.usecase.ToggleUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideToggleUseCase(repository: MovieRepository): ToggleUseCase {
        return ToggleUseCase(repository)
}
}