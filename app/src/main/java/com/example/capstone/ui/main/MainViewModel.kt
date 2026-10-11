package com.example.capstone.ui.main

import androidx.lifecycle.ViewModel
import com.example.capstone.core.domain.usecase.SettingUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class MainViewModel(settingUseCase: SettingUseCase) : ViewModel() {
    val isDarkModeActive: Flow<Boolean> = settingUseCase.isDarkModeActive().distinctUntilChanged()
}
