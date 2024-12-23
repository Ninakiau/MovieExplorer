package com.example.movieexplorer.data.repository


import android.util.Log
import com.example.movieexplorer.data.local.dao.MovieDao
import com.example.movieexplorer.data.local.entities.MovieEntity
import com.example.movieexplorer.data.remote.client.RetrofitClient
import com.example.movieexplorer.data.remote.mapper.MovieMapper
import com.example.movieexplorer.domain.model.Movie
import com.example.movieexplorer.domain.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieDao: MovieDao,
    private val movieMapper: MovieMapper
) : MovieRepository {
    private val api = RetrofitClient.movieApi
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    override suspend fun getPopularMovies(page: Int): Flow<List<Movie>> = flow {
        val response = api.getPopularMovies(page)
        emit(response.movieDtos.map { movieMapper.toDomain(it) })
    }.catch { error ->
        throw error
    }.flowOn(dispatcher)


    override suspend fun searchMovies(query: String, page: Int): Flow<List<Movie>> = flow {
        val response = api.searchMovies(query, page)
        emit(response.movieDtos.map { movieMapper.toDomain(it) })
    }.catch { error ->
        throw error
    }.flowOn(dispatcher)


    override suspend fun getMovieDetails(movieId: Int): Flow<Movie> = flow{
        val response = api.getMovieDetails(movieId)
        emit(movieMapper.toDomain(response))
    }.catch { error ->
        throw error
    }.flowOn(dispatcher)

    override suspend fun addFavorite(movie: MovieEntity) {
        try {
            movieDao.addFavorite(movie)
        } catch (e: Exception) {
            Log.e("Database", "Error al agregar favorito: ${e.message}")
        }
    }


    override suspend fun removeFavorite(movie: MovieEntity) {
        movieDao.removeFavorite(movie)
    }

    override suspend fun getFavoriteMovies(): Flow<List<MovieEntity>> {
        return movieDao.getFavoriteMovies()
            .catch { error ->
                Log.e("MovieRepository", "Error getting favorites: ${error.message}")
                throw error
            }
            .flowOn(Dispatchers.IO)

    }

    override suspend fun isFavorite(movieId: Int): Flow<Boolean> {
        return movieDao.isFavorite(movieId)

    }

    override suspend fun toggleFavorite(movieId: Int) {
        val exists = movieDao.movieExists(movieId)
        val isFavorite = if (exists) movieDao.isFavorite(movieId).first() else false
        Log.d("ToggleFavorite", "movieExists($movieId) = $exists")

        if (isFavorite) {
            // Si es favorito, lo eliminamos
            val response = api.getMovieDetails(movieId)
            Log.d("MovieDetails", "Response from API: $response")
            val domainMovie = movieMapper.toDomain(response)
            Log.d("MovieMapper", "Domain movie mapped: $domainMovie")
            val movieEntity = movieMapper.toEntity(domainMovie)
            Log.d("MovieMapper", "Entity movie mapped: $movieEntity")
            movieDao.removeFavorite(movieEntity)
        } else {
            // Si no es favorito o no existe, lo agregamos
            try {
                val response = api.getMovieDetails(movieId)
                Log.d("MovieDetails", "Response from API: $response")
                val domainMovie = movieMapper.toDomain(response)
                Log.d("MovieMapper", "Domain movie mapped: $domainMovie")
                val movieEntity = movieMapper.toEntity(domainMovie)
                Log.d("MovieMapper", "Entity movie mapped: $movieEntity")
                movieDao.addFavorite(movieEntity)
                movieDao.toggleFavorite(movieId)
            } catch (e: Exception) {
                // Si falla la API, intentamos obtener la película del último listado popular
                getPopularMovies(1).first().find { it.id == movieId }?.let { movie ->
                    movieDao.addFavorite(movieMapper.toEntity(movie))
                }
            }
        }
        // Recuperar favoritos después de cada operación
        val updatedMovies = getFavoriteMovies().first()
        Log.d("UpdatedFavoriteMovies", "Favoritos después de operación: $updatedMovies")
    }


}


