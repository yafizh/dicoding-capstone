package com.example.capstone.core.data.source.local

import com.example.capstone.core.data.source.local.entity.MovieEntity
import com.example.capstone.core.data.source.local.room.MovieDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val movieDao: MovieDao) {

    fun getFavoriteMovies(): Flow<List<MovieEntity>> = movieDao.getFavoriteMovies()

    fun isFavorite(movieId: Int): Flow<Boolean> = movieDao.isFavorite(movieId)

    suspend fun insertFavoriteMovie(movie: MovieEntity) = movieDao.insertFavoriteMovie(movie)

    suspend fun deleteFavoriteMovie(movieId: Int) = movieDao.deleteFavoriteMovie(movieId)
}
