package com.example.movieexplorer.data.remote.mapper

import com.example.movieexplorer.data.remote.dto.MovieDto
import com.example.movieexplorer.domain.model.Movie
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.*

class MovieMapperTest {
    private lateinit var movieMapper: MovieMapper

    private val testMovieDto = MovieDto(
        id = 1,
        adult = false,
        backdropPath = "/backdrop.jpg",
        genreIds = listOf(1, 2, 3),
        originalLanguage = "English",
        originalTitle = "Original Title",
        overview = "Overview",
        popularity = 7.8,
        posterPath = "/poster.jpg",
        releaseDate = "2023-08-01",
        title = "Title",
        video = false,
        voteAverage = 8.5,
        voteCount = 100,
    )

    private val testMovie = Movie(
        id = 1,
        adult = false,
        backdropPath = "/backdrop.jpg",
        genreIds = listOf(1, 2, 3),
        originalLanguage = "English",
        originalTitle = "Original Title",
        overview = "Overview",
        popularity = 7.8,
        posterPath = "/poster.jpg",
        releaseDate = "2023-08-01",
        title = "Title",
        video = false,
        voteAverage = 8.5,
        voteCount = 100,
        posterUrl = "https://image.tmdb.org/t/p/w500/poster.jpg"
    )

    @Before
    fun setUp() {
        movieMapper = MovieMapper()
    }

    @Test
    fun `toDomain maps MovieDto to Movie correctly`() {
        val domain = movieMapper.toDomain(testMovieDto)

        // Verificar todos los campos mapeados
        assertEquals(testMovieDto.id, domain.id)
        assertEquals(testMovieDto.adult, domain.adult)
        assertEquals(testMovieDto.backdropPath, domain.backdropPath)
        assertEquals(testMovieDto.genreIds, domain.genreIds)
        assertEquals(testMovieDto.originalLanguage, domain.originalLanguage)
        assertEquals(testMovieDto.originalTitle, domain.originalTitle)
        assertEquals(testMovieDto.overview, domain.overview)
        assertEquals(testMovieDto.popularity, domain.popularity)
        assertEquals(testMovieDto.posterPath, domain.posterPath)
        assertEquals(testMovieDto.releaseDate, domain.releaseDate)
        assertEquals(testMovieDto.title, domain.title)
        assertEquals(testMovieDto.video, domain.video)
        assertEquals(testMovieDto.voteAverage, domain.voteAverage)
        assertEquals(testMovieDto.voteCount, domain.voteCount)
        assertEquals("https://image.tmdb.org/t/p/w500${testMovieDto.posterPath}", domain.posterUrl)
    }

    @Test
    fun `toDomain handles null posterPath correctly`() {
        val movieDtoWithNullPoster = testMovieDto.copy(posterPath = null)
        val domain = movieMapper.toDomain(movieDtoWithNullPoster)

        assertNull(domain.posterPath)
        assertNull(domain.posterUrl)
    }

    @Test
    fun `toEntity maps Movie to MovieEntity correctly`() {
        val entity = movieMapper.toEntity(testMovie)

        // Verificar todos los campos mapeados
        assertEquals(testMovie.id, entity.id)
        assertEquals(testMovie.title, entity.title)
        assertEquals(false, entity.isFavorite) // Siempre debe ser false inicialmente
        assertEquals(testMovie.overview, entity.overview)
        assertEquals(testMovie.posterPath, entity.posterPath)
        assertEquals(testMovie.releaseDate, entity.releaseDate)
        assertEquals(testMovie.adult, entity.adult)
        assertEquals(testMovie.backdropPath, entity.backdropPath)
        assertEquals(testMovie.originalLanguage, entity.originalLanguage)
        assertEquals(testMovie.originalTitle, entity.originalTitle)
        assertEquals(testMovie.popularity, entity.popularity)
        assertEquals(testMovie.video, entity.video)
        assertEquals(testMovie.voteAverage, entity.voteAverage)
        assertEquals(testMovie.voteCount, entity.voteCount)
        assertEquals(testMovie.posterUrl, entity.posterUrl)
    }

    @Test
    fun `toEntity sets isFavorite to false by default`() {
        val movieWithFavorite = testMovie.copy()
        val entity = movieMapper.toEntity(movieWithFavorite)

        assertEquals(false, entity.isFavorite)
    }
}