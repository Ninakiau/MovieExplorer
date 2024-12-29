package com.example.movieexplorer.data.repository

import com.example.movieexplorer.data.local.dao.MovieDao
import com.example.movieexplorer.data.local.entities.MovieEntity
import com.example.movieexplorer.data.remote.api.MovieApi
import com.example.movieexplorer.data.remote.client.RetrofitClient
import com.example.movieexplorer.data.remote.dto.MovieDto
import com.example.movieexplorer.data.remote.dto.MovieResponse
import com.example.movieexplorer.data.remote.mapper.MovieMapper
import com.example.movieexplorer.domain.model.Movie
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class MovieRepositoryImplTest {
     private lateinit var movieRepository: MovieRepositoryImpl

     @MockK
     private lateinit var movieDao: MovieDao

     @MockK
     private lateinit var movieMapper: MovieMapper


     @MockK
     private lateinit var movieApi: MovieApi

     private val testMovieEntity = MovieEntity(
          id = 1,
          title = "Test Movie",
          isFavorite = false,
          overview = "Test Overview",
          posterPath = "test_poster_path",
          releaseDate = "2023-08-01",
          adult = false,
          backdropPath = "test_backdrop_path",
          originalLanguage = "en",
          originalTitle = "Test Original Title",
          popularity = 7.8,
          video = false,
          voteAverage = 8.5,
          voteCount = 100,
          posterUrl = "test_poster_url"
     )

     private val testMovie = Movie(
          id = 1,
          title = "Test Movie",
          overview = "Test Overview",
          posterPath = "test_poster_path",
          releaseDate = "2023-08-01",
          adult = false,
          backdropPath = "test_backdrop_path",
          originalLanguage = "en",
          originalTitle = "Test Original Title",
          popularity = 7.8,
          video = false,
          voteAverage = 8.5,
          voteCount = 100,
          posterUrl = "test_poster_url",
          genreIds = listOf(1, 2, 3),
     )

     private val testMovieDto = MovieDto(
          id = 1,
          title = "Test Movie",
          overview = "Test Overview",
          posterPath = "test_poster_path",
          releaseDate = "2023-08-01",
          adult = false,
          backdropPath = "test_backdrop_path",
          originalLanguage = "en",
          originalTitle = "Test Original Title",
          popularity = 7.8,
          video = false,
          voteAverage = 8.5,
          voteCount = 100,
          genreIds = listOf(1, 2, 3),
     )

     @Before
     fun setUp() {
          MockKAnnotations.init(this)
          mockkObject(RetrofitClient)
          every { RetrofitClient.movieApi } returns movieApi
          movieRepository = MovieRepositoryImpl(movieDao, movieMapper)
     }

     @Test
     fun `getPopularMovies should return mapped movies`() = runBlocking {
          // Arrange
          val movieResponse = MovieResponse(1, listOf(testMovieDto), 1, 1)
          coEvery { movieApi.getPopularMovies(1) } returns movieResponse
          coEvery { movieMapper.toDomain(any()) } returns testMovie

          // Act
          val result = movieRepository.getPopularMovies(1).firstOrNull()

          // Assert
          assertEquals(listOf(testMovie), result)
          coVerify { movieApi.getPopularMovies(1) }
          coVerify { movieMapper.toDomain(testMovieDto) }
     }

     @Test
     fun `searchMovies should return mapped movies`() = runBlocking {
          // Arrange
          val query = "test"
          val movieResponse = MovieResponse(1, listOf(testMovieDto), 1, 1)
          coEvery { movieApi.searchMovies(query, 1) } returns movieResponse
          coEvery { movieMapper.toDomain(any()) } returns testMovie

          // Act
          val result = movieRepository.searchMovies(query, 1).firstOrNull()

          // Assert
          assertEquals(listOf(testMovie), result)
          coVerify { movieApi.searchMovies(query, 1) }
          coVerify { movieMapper.toDomain(testMovieDto) }
     }

     @Test
     fun `getMovieDetails should return mapped movie`() = runBlocking {
          // Arrange
          coEvery { movieApi.getMovieDetails(1) } returns testMovieDto
          coEvery { movieMapper.toDomain(any()) } returns testMovie

          // Act
          val result = movieRepository.getMovieDetails(1).firstOrNull()

          // Assert
          assertEquals(testMovie, result)
          coVerify { movieApi.getMovieDetails(1) }
          coVerify { movieMapper.toDomain(testMovieDto) }
     }

     @Test
     fun `addFavorite should call movieDao addFavorite`() = runBlocking {
          // Arrange
          coEvery { movieDao.addFavorite(testMovieEntity) } just Runs

          // Act
          movieRepository.addFavorite(testMovieEntity)

          // Assert
          coVerify { movieDao.addFavorite(testMovieEntity) }
     }

     @Test
     fun `removeFavorite should call movieDao removeFavorite`() = runBlocking {
          // Arrange
          coEvery { movieDao.removeFavorite(testMovieEntity) } just Runs

          // Act
          movieRepository.removeFavorite(testMovieEntity)

          // Assert
          coVerify { movieDao.removeFavorite(testMovieEntity) }
     }

     @Test
     fun `getFavoriteMovies should return movies from dao`() = runBlocking {
          // Arrange
          val favoriteMovies = listOf(testMovieEntity)
          coEvery { movieDao.getFavoriteMovies() } returns flow { emit(favoriteMovies) }

          // Act
          val result = movieRepository.getFavoriteMovies().firstOrNull()

          // Assert
          assertEquals(favoriteMovies, result)
          coVerify { movieDao.getFavoriteMovies() }
     }

     @Test
     fun `isFavorite should return true when movie is favorite`() = runBlocking {
          // Arrange
          coEvery { movieDao.isFavorite(1) } returns flow { emit(true) }

          // Act
          val result = movieRepository.isFavorite(1).firstOrNull()

          // Assert
          assertTrue(result!!)
          coVerify { movieDao.isFavorite(1) }
     }

     @Test
     fun `toggleFavorite should remove movie when it is favorite`() = runBlocking {
          // Arrange
          coEvery { movieDao.movieExists(1) } returns true
          coEvery { movieDao.isFavorite(1) } returns flow { emit(true) }
          coEvery { movieApi.getMovieDetails(1) } returns testMovieDto
          coEvery { movieMapper.toDomain(testMovieDto) } returns testMovie
          coEvery { movieMapper.toEntity(testMovie) } returns testMovieEntity
          coEvery { movieDao.removeFavorite(testMovieEntity) } just Runs
          coEvery { movieDao.getFavoriteMovies() } returns flow { emit(emptyList()) }

          // Act
          movieRepository.toggleFavorite(1)

          // Assert
          coVerify(exactly = 1) {
               movieDao.movieExists(1)
               movieDao.isFavorite(1)
               movieApi.getMovieDetails(1)
               movieMapper.toDomain(testMovieDto)
               movieMapper.toEntity(testMovie)
               movieDao.removeFavorite(testMovieEntity)
               movieDao.getFavoriteMovies()
          }
     }

     @Test
     fun `toggleFavorite should add movie when it is not favorite`() = runBlocking {
          // Arrange
          coEvery { movieDao.movieExists(1) } returns false
          coEvery { movieApi.getMovieDetails(1) } returns testMovieDto
          coEvery { movieMapper.toDomain(testMovieDto) } returns testMovie
          coEvery { movieMapper.toEntity(testMovie) } returns testMovieEntity
          coEvery { movieDao.addFavorite(testMovieEntity) } just Runs
          coEvery { movieDao.toggleFavorite(1) } just Runs
          coEvery { movieDao.getFavoriteMovies() } returns flow { emit(listOf(testMovieEntity)) }

          // Act
          movieRepository.toggleFavorite(1)

          // Assert
          coVerify(exactly = 1) {
               movieDao.movieExists(1)
               movieApi.getMovieDetails(1)
               movieMapper.toDomain(testMovieDto)
               movieMapper.toEntity(testMovie)
               movieDao.addFavorite(testMovieEntity)
               movieDao.toggleFavorite(1)
               movieDao.getFavoriteMovies()
          }
     }
}