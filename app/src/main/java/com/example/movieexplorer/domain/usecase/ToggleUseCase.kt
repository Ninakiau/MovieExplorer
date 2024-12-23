package com.example.movieexplorer.domain.usecase


import com.example.movieexplorer.domain.repository.MovieRepository
import javax.inject.Inject

class ToggleUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    suspend operator fun invoke(movieId: Int) = repository.toggleFavorite(movieId)
}