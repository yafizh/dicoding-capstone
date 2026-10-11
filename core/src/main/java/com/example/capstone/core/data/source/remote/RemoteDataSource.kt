package com.example.capstone.core.data.source.remote

import com.example.capstone.core.data.source.remote.network.ApiResponse
import com.example.capstone.core.data.source.remote.network.ApiService
import com.example.capstone.core.data.source.remote.response.ListMovieResponse
import com.example.capstone.core.data.source.remote.response.MovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException

class RemoteDataSource(private val apiService: ApiService) {

    fun getPopularMovies(): Flow<ApiResponse<List<MovieResponse>>> =
        fetchMovies { apiService.getPopularMovies() }

    fun searchMovies(query: String): Flow<ApiResponse<List<MovieResponse>>> =
        fetchMovies { apiService.searchMovies(query) }

    private fun fetchMovies(
        request: suspend () -> ListMovieResponse,
    ): Flow<ApiResponse<List<MovieResponse>>> = flow {
        val movies = request().results.orEmpty()
        if (movies.isNotEmpty()) {
            emit(ApiResponse.Success(movies))
        } else {
            emit(ApiResponse.Empty)
        }
    }.catch { throwable ->
        emit(ApiResponse.Error(throwable.toErrorMessage()))
    }.flowOn(Dispatchers.IO)

    private fun Throwable.toErrorMessage(): String = when (this) {
        is HttpException -> when (code()) {
            401 -> "Invalid TMDB API key. Please check your local.properties."
            404 -> "Data not found."
            else -> "Server error (${code()}). Please try again later."
        }

        is IOException -> "No internet connection. Please check your network."
        else -> message ?: "Something went wrong."
    }
}
