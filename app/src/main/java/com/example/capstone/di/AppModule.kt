package com.example.capstone.di

import com.example.capstone.core.domain.usecase.MovieInteractor
import com.example.capstone.core.domain.usecase.MovieUseCase
import com.example.capstone.core.domain.usecase.SettingInteractor
import com.example.capstone.core.domain.usecase.SettingUseCase
import com.example.capstone.ui.detail.DetailViewModel
import com.example.capstone.ui.home.HomeViewModel
import com.example.capstone.ui.main.MainViewModel
import com.example.capstone.ui.setting.SettingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<MovieUseCase> { MovieInteractor(get()) }
    factory<SettingUseCase> { SettingInteractor(get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { SettingViewModel(get()) }
}
