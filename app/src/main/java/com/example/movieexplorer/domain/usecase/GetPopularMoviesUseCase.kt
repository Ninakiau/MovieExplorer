package com.example.movieexplorer.domain.usecase

import com.example.movieexplorer.data.repository.MovieRepositoryImpl
import com.example.movieexplorer.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(
    private val repository: MovieRepositoryImpl
) {
    suspend operator fun invoke(page: Int): Flow<List<Movie>> = repository.getPopularMovies(page)
}