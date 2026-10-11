package com.example.capstone.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstone.core.domain.usecase.MovieUseCase
import com.example.capstone.model.MovieModel
import com.example.capstone.model.toModels
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FavoriteViewModel(movieUseCase: MovieUseCase) : ViewModel() {

    // null means the database hasn't emitted yet.
    val favoriteMovies: StateFlow<List<MovieModel>?> = movieUseCase.getFavoriteMovies()
        .map { it.toModels() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )
}
