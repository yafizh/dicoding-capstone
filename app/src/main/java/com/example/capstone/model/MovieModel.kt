package com.example.capstone.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieModel(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val posterUrl: String?,
    val backdropUrl: String?,
    val releaseDate: String,
    val formattedReleaseDate: String,
    val voteAverage: Double,
    val formattedRating: String,
) : Parcelable
