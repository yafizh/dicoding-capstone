package com.example.capstone.core.domain.usecase

import com.example.capstone.core.domain.model.Movie
import com.example.capstone.core.domain.repository.IMovieRepository

class MovieInteractor(private val movieRepository: IMovieRepository) : MovieUseCase {

    override fun getPopularMovies() = movieRepository.getPopularMovies()

    override fun searchMovies(query: String) = movieRepository.searchMovies(query)

    override fun getFavoriteMovies() = movieRepository.getFavoriteMovies()

    override fun isFavorite(movieId: Int) = movieRepository.isFavorite(movieId)

    override suspend fun setFavoriteMovie(movie: Movie, state: Boolean) =
        movieRepository.setFavoriteMovie(movie, state)
}
