package com.example.movieexplorer.domain.usecase

import com.example.movieexplorer.data.repository.MovieRepositoryImpl
import com.example.movieexplorer.domain.model.Movie
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.junit.*

class SearchMoviesUseCaseTest {

    private lateinit var repository: MovieRepositoryImpl
    private lateinit var searchMoviesUseCase: SearchMoviesUseCase

    @Before
    fun setUp() {
        repository = mockk()
        searchMoviesUseCase = SearchMoviesUseCase(repository)
    }

    @Test
    fun `invoke should return movies when searching with valid query`() = runBlocking {
        // Given
        val query = "Batman"
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
        coEvery { repository.searchMovies(query, page) } returns flowOf(expectedMovies)

        // When
        val result = searchMoviesUseCase(query, page).single()

        // Then
        assertEquals(expectedMovies, result)
        coVerify(exactly = 1) { repository.searchMovies(query, page) }
    }

    @Test
    fun `invoke should return empty list when no movies match search query`() = runBlocking {
        // Given
        val query = "NonexistentMovie"
        val page = 1
        val expectedMovies = emptyList<Movie>()
        coEvery { repository.searchMovies(query, page) } returns flowOf(expectedMovies)

        // When
        val result = searchMoviesUseCase(query, page).single()

        // Then
        assertEquals(expectedMovies, result)
        coVerify(exactly = 1) { repository.searchMovies(query, page) }
    }

    @Test
    fun `invoke should handle empty query string`() = runBlocking {
        // Given
        val query = ""
        val page = 1
        val expectedMovies = emptyList<Movie>()
        coEvery { repository.searchMovies(query, page) } returns flowOf(expectedMovies)

        // When
        val result = searchMoviesUseCase(query, page).single()

        // Then
        assertEquals(expectedMovies, result)
        coVerify(exactly = 1) { repository.searchMovies(query, page) }
    }
}