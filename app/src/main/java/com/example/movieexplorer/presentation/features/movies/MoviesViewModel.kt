package com.example.movieexplorer.presentation.features.movies

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope
import com.example.movieexplorer.data.local.entities.MovieEntity
import com.example.movieexplorer.domain.model.Movie
import com.example.movieexplorer.domain.repository.MovieRepository
import com.example.movieexplorer.domain.usecase.GetPopularMoviesUseCase
import com.example.movieexplorer.domain.usecase.SearchMoviesUseCase
import com.example.movieexplorer.domain.usecase.ToggleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val toggleUseCase: ToggleUseCase,
    private val movieRepository: MovieRepository
) : ViewModel() {



    private val _uiState = MutableStateFlow(MovieUIState())
    val uiState: StateFlow<MovieUIState> = _uiState.asStateFlow()



    var searchQuery by mutableStateOf(TextFieldValue(""))
        private set

    private var searchJob: Job? = null

    private var currentPage = 1

    init {
        loadMovies()
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            Log.d("MovieViewModel", "Starting to observe favorites")
            try {
                movieRepository.getFavoriteMovies()
                    .collect { favoriteMovies ->
                        Log.d("MovieViewModel", "Received favorites: $favoriteMovies")
                        _uiState.update { currentState ->
                            currentState.copy(
                                favoriteMovies = favoriteMovies.map { it.id }.toSet()
                            )
                        }
                        Log.d("MovieViewModel", "Updated UI state with favorites")
                    }
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Error in observeFavorites: ${e.message}", e)
            }
        }
    }



    fun loadMovies() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                getPopularMoviesUseCase(page = currentPage)
                    .collect { movies ->
                        _uiState.update {
                            it.copy(
                                movies = movies,
                                isLoading = false
                            )
                        }

                    }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }

        }
    }

    fun toggleFavorite (movieId: Int) {
        viewModelScope.launch {
            try {
                toggleUseCase(movieId )
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }


    fun searchMovies(query: TextFieldValue) {
        searchQuery = query

        searchJob?.cancel()

        if (query.text.isEmpty()) {
            loadMovies()
            return
        }

        searchJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(300)

            try {
                searchMoviesUseCase(query = query.text, page = 1)
                    .collect { searchResults ->
                        _uiState.update {
                            it.copy(
                                movies = searchResults,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }
}


data class MovieUIState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val favoriteMovies: Set<Int> = emptySet()

)