package com.example.capstone.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstone.core.domain.usecase.SettingUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingViewModel(private val settingUseCase: SettingUseCase) : ViewModel() {

    val isDarkModeActive: StateFlow<Boolean?> = settingUseCase.isDarkModeActive()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    fun setDarkModeActive(isActive: Boolean) {
        viewModelScope.launch {
            settingUseCase.setDarkModeActive(isActive)
        }
    }
}
