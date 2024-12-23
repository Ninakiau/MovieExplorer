package com.example.movieexplorer.presentation.navigation

sealed class Screen(val route: String) {
    object Movies : Screen("movies")
    object Favorites : Screen("favorites")
    object MovieDetail : Screen("movie/{movieId}") {
        fun createRoute(movieId: Int) = "movie/$movieId"
    }
}