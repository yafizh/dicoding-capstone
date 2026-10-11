package com.example.capstone.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstone.core.domain.model.Resource
import com.example.capstone.core.domain.usecase.MovieUseCase
import com.example.capstone.model.MovieModel
import com.example.capstone.model.toModelResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class HomeViewModel(private val movieUseCase: MovieUseCase) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val retryTrigger = MutableStateFlow(0)

    val movies: StateFlow<Resource<List<MovieModel>>> = combine(
        searchQuery
            .debounce(SEARCH_DEBOUNCE_MILLIS)
            .map { it.trim() }
            .distinctUntilChanged(),
        retryTrigger,
    ) { query, _ -> query }
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                movieUseCase.getPopularMovies()
            } else {
                movieUseCase.searchMovies(query)
            }
        }
        .map { it.toModelResource() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Resource.Loading,
        )

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun retry() {
        retryTrigger.value += 1
    }

    private companion object {
        const val SEARCH_DEBOUNCE_MILLIS = 500L
    }
}
