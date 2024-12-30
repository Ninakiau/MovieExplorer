package com.example.movieexplorer.domain.usecase

import com.example.movieexplorer.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.*

class ToggleUseCaseTest {

    private lateinit var repository: MovieRepository
    private lateinit var toggleUseCase: ToggleUseCase

    @Before
    fun setUp() {
        repository = mockk()
        toggleUseCase = ToggleUseCase(repository)
    }

    @Test
    fun `invoke should successfully toggle movie favorite status`() = runBlocking {
        // Given
        val movieId = 1
        coEvery { repository.toggleFavorite(movieId) } returns Unit

        // When
        toggleUseCase(movieId)

        // Then
        coVerify(exactly = 1) { repository.toggleFavorite(movieId) }
    }

    @Test
    fun `invoke should handle invalid movie id`() = runBlocking {
        // Given
        val invalidMovieId = -1
        coEvery { repository.toggleFavorite(invalidMovieId) } throws IllegalArgumentException("Invalid movie ID")

        // When & Then
        try {
            toggleUseCase(invalidMovieId)
        } catch (e: IllegalArgumentException) {
            assert(e.message == "Invalid movie ID")
        }
        coVerify(exactly = 1) { repository.toggleFavorite(invalidMovieId) }
    }
}