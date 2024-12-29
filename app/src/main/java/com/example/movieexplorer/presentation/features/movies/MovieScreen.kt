package com.example.movieexplorer.presentation.features.movies

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.example.movieexplorer.domain.model.Movie


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreen(
    viewModel: MovieViewModel,
    onNavigateToFavorites: () -> Unit,
    onNavigateToDetail: (Int) -> Unit

) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Botón de favoritos en su propia fila
            Button(
                onClick = { onNavigateToFavorites() },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 1.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 1.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Ver Favoritos",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            SearchBar(
                query = viewModel.searchQuery.text,
                onQueryChange = { viewModel.searchMovies(TextFieldValue(it)) },
                onSearch = { },
                active = false,
                onActiveChange = { },
                modifier = Modifier.fillMaxWidth()
                .padding(bottom=8.dp),
                placeholder = { Text("Search movies...") },
                content = { }
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                uiState.error != null -> {
                    Text(
                        "Error type ${uiState.error}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                uiState.movies.isEmpty() -> {
                    Text(
                        text = "No movies found",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
                else -> {
                    MovieList(
                        uiState.movies,
                        favoriteMovies = uiState.favoriteMovies,
                        onFavoriteClick = viewModel::toggleFavorite,
                        onMovieClick = onNavigateToDetail
                    )
                }
            }

            Button(onClick = { viewModel.loadMovies() }) {
                Text("Load")
            }
        }
    }
}

@Composable
fun MovieList(
    movies: List<Movie>,
    favoriteMovies: Set<Int>,
    onFavoriteClick: (Int) -> Unit,
    onMovieClick: (Int) -> Unit
) {
    LazyColumn {
        items(movies) { movie ->
            MovieItem(
                movie = movie,
                favoriteMovies,
                onFavoriteClick = onFavoriteClick,
                onMovieClick = onMovieClick
            )
        }
    }
}

@Composable
fun MovieItem(movie: Movie,
              favoriteMovies: Set<Int>,
              onFavoriteClick: (Int) -> Unit = {},
              onMovieClick: (Int) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        onClick = { onMovieClick(movie.id) }
    ) {
        Column(Modifier.padding(16.dp)) {
            movie.posterUrl?.let { url ->
                Image(
                    painter = rememberAsyncImagePainter(url),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
            } ?: Box(  // Placeholder cuando no hay imagen
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = movie.title, style = MaterialTheme.typography.headlineMedium)
            movie.releaseDate?.let { Text(text = it, style = MaterialTheme.typography.bodySmall) }
            Spacer(modifier = Modifier.height(8.dp))
            movie.overview?.let { Text(text = it, style = MaterialTheme.typography.bodyMedium) }
            Spacer(modifier = Modifier.height(8.dp))
            IconButton(onClick = { onFavoriteClick(movie.id) }) {
                Icon(
                    imageVector = if (movie.id in favoriteMovies) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Filled.FavoriteBorder
                    },
                    contentDescription = if (movie.id in favoriteMovies) {
                        "Remove from Favorites"
                    } else {
                        "Add to Favorites"
                    },
                    tint = if (movie.id in favoriteMovies) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )

            }

        }
    }
}
