package com.example.capstone.core.data

import com.example.capstone.core.data.source.local.LocalDataSource
import com.example.capstone.core.data.source.remote.RemoteDataSource
import com.example.capstone.core.data.source.remote.network.ApiResponse
import com.example.capstone.core.data.source.remote.response.MovieResponse
import com.example.capstone.core.domain.model.Movie
import com.example.capstone.core.domain.model.Resource
import com.example.capstone.core.domain.repository.IMovieRepository
import com.example.capstone.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class MovieRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : IMovieRepository {

    override fun getPopularMovies(): Flow<Resource<List<Movie>>> =
        remoteDataSource.getPopularMovies().asResource()

    override fun searchMovies(query: String): Flow<Resource<List<Movie>>> =
        remoteDataSource.searchMovies(query).asResource()

    override fun getFavoriteMovies(): Flow<List<Movie>> =
        localDataSource.getFavoriteMovies().map { DataMapper.mapEntitiesToDomain(it) }

    override fun isFavorite(movieId: Int): Flow<Boolean> =
        localDataSource.isFavorite(movieId)

    override suspend fun setFavoriteMovie(movie: Movie, state: Boolean) {
        if (state) {
            localDataSource.insertFavoriteMovie(DataMapper.mapDomainToEntity(movie))
        } else {
            localDataSource.deleteFavoriteMovie(movie.id)
        }
    }

    private fun Flow<ApiResponse<List<MovieResponse>>>.asResource(): Flow<Resource<List<Movie>>> =
        map { response ->
            when (response) {
                is ApiResponse.Success -> Resource.Success(DataMapper.mapResponsesToDomain(response.data))
                is ApiResponse.Empty -> Resource.Success(emptyList())
                is ApiResponse.Error -> Resource.Error(response.errorMessage)
            }
        }.onStart { emit(Resource.Loading) }
}
