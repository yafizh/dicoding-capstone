package com.example.capstone.model

import com.example.capstone.core.domain.model.Movie
import com.example.capstone.core.domain.model.Resource
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

private const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500"
private const val BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w780"
private const val UNKNOWN_DATE = "-"

private val displayDateFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.getDefault())

fun Movie.toModel(): MovieModel = MovieModel(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    posterUrl = posterPath?.let { POSTER_BASE_URL + it },
    backdropUrl = (backdropPath ?: posterPath)?.let { BACKDROP_BASE_URL + it },
    releaseDate = releaseDate,
    formattedReleaseDate = formatReleaseDate(releaseDate),
    voteAverage = voteAverage,
    formattedRating = String.format(Locale.US, "%.1f", voteAverage),
)

fun MovieModel.toDomain(): Movie = Movie(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
)

fun List<Movie>.toModels(): List<MovieModel> = map { it.toModel() }

fun Resource<List<Movie>>.toModelResource(): Resource<List<MovieModel>> = when (this) {
    is Resource.Loading -> Resource.Loading
    is Resource.Success -> Resource.Success(data.toModels())
    is Resource.Error -> Resource.Error(message)
}

private fun formatReleaseDate(date: String): String {
    if (date.isBlank()) return UNKNOWN_DATE
    return try {
        LocalDate.parse(date).format(displayDateFormatter)
    } catch (e: DateTimeParseException) {
        date
    }
}
