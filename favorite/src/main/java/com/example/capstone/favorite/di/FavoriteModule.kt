package com.example.capstone.favorite.di

import com.example.capstone.favorite.FavoriteViewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val favoriteModule = module {
    viewModel { FavoriteViewModel(get()) }
}

// The dynamic feature can't be referenced from :app, so it registers its own module on first use.
private val loadFavoriteModule by lazy { loadKoinModules(favoriteModule) }

fun injectFavoriteModule() = loadFavoriteModule
