package com.example.movieexplorer.domain.repository

import com.example.movieexplorer.data.local.entities.MovieEntity
import com.example.movieexplorer.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getPopularMovies(page: Int): Flow<List<Movie>>
    suspend fun searchMovies(query: String, page: Int): Flow<List<Movie>>
    suspend fun getMovieDetails(movieId: Int): Flow<Movie>
    suspend fun addFavorite(movie: MovieEntity)
    suspend fun removeFavorite(movie: MovieEntity)
    suspend fun getFavoriteMovies(): Flow<List<MovieEntity>>
    suspend fun isFavorite(movieId: Int): Flow<Boolean>
    suspend fun toggleFavorite(movieId: Int)
}