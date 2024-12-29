package com.example.movieexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movieexplorer.presentation.features.details.MovieDetailScreen
import com.example.movieexplorer.presentation.features.movies.MovieScreen
import com.example.movieexplorer.presentation.features.favorite.FavoriteMoviesScreen
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
            val isDarkMode by viewModel.themeState.collectAsState()
            MovieExplorerTheme(darkTheme = isDarkMode.isDarkMode) {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Movie Explorer",
                                    modifier = Modifier.fillMaxWidth(),
                                    style = MaterialTheme.typography.headlineMedium,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            actions = {
                                IconButton(onClick = { viewModel.toggleTheme() }) {
                                    Icon(
                                        imageVector = if (isDarkMode.isDarkMode) Icons.Default.LightMode
                                        else Icons.Default.DarkMode,
                                        contentDescription = "Toggle theme"
                                    )
                                }
                            },
                        )
                    }
                ) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
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
                            }
                            composable(
                                "detail/{movieId}",
                                arguments = listOf(navArgument("movieId") { type = NavType.IntType })
                            ) { backStackEntry ->
                                val movieId = backStackEntry.arguments?.getInt("movieId") ?: -1
                                MovieDetailScreen(
                                    movieId = movieId,
                                    viewModel = viewModel,
                                    onNavigateBack = { navController.popBackStack() }
                                )
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