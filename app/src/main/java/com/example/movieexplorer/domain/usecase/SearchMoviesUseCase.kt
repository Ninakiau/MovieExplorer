package com.example.movieexplorer.domain.usecase

import com.example.movieexplorer.data.repository.MovieRepositoryImpl
import com.example.movieexplorer.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val repository: MovieRepositoryImpl
) {

    suspend operator fun invoke(query: String, page: Int) : Flow<List<Movie>> = repository.searchMovies(query, page)
}