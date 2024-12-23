package com.example.movieexplorer.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "movies")
data class MovieEntity (
    @PrimaryKey val id: Int,
    val title: String?,
    val isFavorite: Boolean?,
    val overview: String?,
    val posterPath: String?,
    val releaseDate: String?,
    val adult: Boolean?,
    val backdropPath: String?,
    val originalLanguage: String?,
    val originalTitle: String?,
    val popularity: Double?,
    val video: Boolean?,
    val voteAverage: Double?,
    val voteCount: Int?,
    val posterUrl: String?
)


