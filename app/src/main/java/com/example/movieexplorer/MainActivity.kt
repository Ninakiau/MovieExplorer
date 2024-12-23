package com.example.movieexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movieexplorer.presentation.features.details.MovieDetailScreen
import com.example.movieexplorer.presentation.features.movies.MovieScreen
import com.example.movieexplorer.presentation.features.movies.FavoriteMoviesScreen
import com.example.movieexplorer.presentation.features.movies.MovieViewModel
import com.example.movieexplorer.presentation.ui.theme.MovieExplorerTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel: MovieViewModel by viewModels()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieExplorerTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        // Configuración del NavHost
                        NavHost(
                            navController = navController,
                            startDestination = "movies"
                        ) {
                            composable("movies") {
                                MovieScreen(
                                    viewModel = viewModel,
                                    onNavigateToFavorites = { navController.navigate("favorites") },
                                    onNavigateToDetail = { movieId -> navController.navigate("detail/$movieId") }
                                )
                            }
                            composable("favorites") {
                                FavoriteMoviesScreen(
                                    viewModel = viewModel,
                                    onNavigateBack = { navController.popBackStack() },
                                    onNavigateToDetail = { movieId -> navController.navigate("detail/$movieId") },
                                )
                            // Define tu pantalla de favoritos aquí
                            }
                            composable("detail/{movieId}") { backStackEntry ->
                                val movieId = backStackEntry.arguments?.getString("movieId")
                                MovieDetailScreen (
                                    movieId = movieId,
                                    viewModel = viewModel,
                                    onNavigateBack = { navController.popBackStack() }) // Define tu pantalla de detalles aquí
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MovieExplorerTheme {
        Greeting("Android")
    }
}