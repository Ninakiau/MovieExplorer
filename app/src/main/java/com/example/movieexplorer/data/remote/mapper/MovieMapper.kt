package com.example.movieexplorer.data.remote.mapper



import com.example.movieexplorer.data.local.entities.MovieEntity
import com.example.movieexplorer.data.remote.dto.MovieDto
import com.example.movieexplorer.domain.model.Movie

class MovieMapper {
    fun toDomain(dto: MovieDto): Movie =
        Movie(
            id = dto.id,
            adult = dto.adult,
            backdropPath = dto.backdropPath,
            genreIds = dto.genreIds,
            originalLanguage = dto.originalLanguage,
            originalTitle = dto.originalTitle,
            overview = dto.overview,
            popularity = dto.popularity,
            posterPath = dto.posterPath,
            releaseDate = dto.releaseDate,
            title = dto.title,
            video = dto.video,
            voteAverage = dto.voteAverage,
            voteCount = dto.voteCount,
            posterUrl = dto.posterPath?.let {
                "https://image.tmdb.org/t/p/w500$it"
            }
        )

    fun toEntity(domain: Movie): MovieEntity =
        MovieEntity(
            id = domain.id,
            title = domain.title,
            isFavorite = false,
            overview = domain.overview,
            posterPath = domain.posterPath,
            releaseDate = domain.releaseDate,
            adult = domain.adult,
            backdropPath = domain.backdropPath,
            originalLanguage = domain.originalLanguage,
            originalTitle = domain.originalTitle,
            popularity = domain.popularity,
            video = domain.video,
            voteAverage = domain.voteAverage,
            voteCount = domain.voteCount,
            posterUrl = domain.posterUrl
        )
}