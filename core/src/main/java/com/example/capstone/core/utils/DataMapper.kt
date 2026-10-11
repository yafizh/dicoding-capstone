package com.example.capstone.core.utils

import com.example.capstone.core.data.source.local.entity.MovieEntity
import com.example.capstone.core.data.source.remote.response.MovieResponse
import com.example.capstone.core.domain.model.Movie

object DataMapper {

    fun mapResponsesToDomain(input: List<MovieResponse>): List<Movie> =
        input.map { response ->
            Movie(
                id = response.id,
                title = response.title.orEmpty(),
                overview = response.overview.orEmpty(),
                posterPath = response.posterPath,
                backdropPath = response.backdropPath,
                releaseDate = response.releaseDate.orEmpty(),
                voteAverage = response.voteAverage ?: 0.0,
            )
        }

    fun mapEntitiesToDomain(input: List<MovieEntity>): List<Movie> =
        input.map { entity ->
            Movie(
                id = entity.id,
                title = entity.title,
                overview = entity.overview,
                posterPath = entity.posterPath,
                backdropPath = entity.backdropPath,
                releaseDate = entity.releaseDate,
                voteAverage = entity.voteAverage,
            )
        }

    fun mapDomainToEntity(input: Movie): MovieEntity =
        MovieEntity(
            id = input.id,
            title = input.title,
            overview = input.overview,
            posterPath = input.posterPath,
            backdropPath = input.backdropPath,
            releaseDate = input.releaseDate,
            voteAverage = input.voteAverage,
        )
}
