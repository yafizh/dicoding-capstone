package com.example.capstone.core.domain.usecase

import kotlinx.coroutines.flow.Flow

interface SettingUseCase {
    fun isDarkModeActive(): Flow<Boolean>

    suspend fun setDarkModeActive(isActive: Boolean)
}
