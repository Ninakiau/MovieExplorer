package com.example.movieexplorer.domain.usecase

import com.example.movieexplorer.data.repository.MovieRepositoryImpl
import com.example.movieexplorer.domain.model.Movie
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.junit.*

class GetPopularMoviesUseCaseTest {
    @MockK
    private lateinit var repository: MovieRepositoryImpl
    private lateinit var getPopularMoviesUseCase: GetPopularMoviesUseCase

    @Before
    fun setUp() {
        repository = mockk()
        getPopularMoviesUseCase = GetPopularMoviesUseCase(repository)
    }

    @Test
    fun `invoke should return movies from repository`() = runBlocking {
        // Given
        val page = 1
        val expectedMovies = listOf(
            Movie(
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
        )
        coEvery { repository.getPopularMovies(page) } returns flowOf(expectedMovies)

        // When
        val result = getPopularMoviesUseCase(page).single()

        // Then
        assertEquals(expectedMovies, result)
        coVerify(exactly = 1) { repository.getPopularMovies(page) }
    }

    @Test
    fun `invoke should return empty list when repository returns empty`() = runBlocking {
        // Given
        val page = 1
        val expectedMovies = emptyList<Movie>()
        coEvery { repository.getPopularMovies(page) } returns flowOf(expectedMovies)

        // When
        val result = getPopularMoviesUseCase(page).single()

        // Then
        assertEquals(expectedMovies, result)
        coVerify(exactly = 1) { repository.getPopularMovies(page) }
    }
}