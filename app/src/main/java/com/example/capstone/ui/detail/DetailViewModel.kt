package com.example.capstone.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstone.core.domain.usecase.MovieUseCase
import com.example.capstone.model.MovieModel
import com.example.capstone.model.toDomain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModel(private val movieUseCase: MovieUseCase) : ViewModel() {

    private val movieId = MutableStateFlow<Int?>(null)

    val isFavorite: StateFlow<Boolean> = movieId
        .filterNotNull()
        .flatMapLatest { id -> movieUseCase.isFavorite(id) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    fun setMovieId(id: Int) {
        movieId.value = id
    }

    fun setFavorite(movie: MovieModel, state: Boolean) {
        viewModelScope.launch {
            movieUseCase.setFavoriteMovie(movie.toDomain(), state)
        }
    }
}
