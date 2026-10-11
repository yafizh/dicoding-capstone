package com.example.capstone.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface ISettingRepository {
    fun isDarkModeActive(): Flow<Boolean>

    suspend fun setDarkModeActive(isActive: Boolean)
}
