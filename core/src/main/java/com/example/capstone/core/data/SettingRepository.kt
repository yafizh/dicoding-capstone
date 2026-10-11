package com.example.capstone.core.data

import com.example.capstone.core.data.source.local.preferences.SettingPreferences
import com.example.capstone.core.domain.repository.ISettingRepository
import kotlinx.coroutines.flow.Flow

class SettingRepository(private val settingPreferences: SettingPreferences) : ISettingRepository {

    override fun isDarkModeActive(): Flow<Boolean> = settingPreferences.isDarkModeActive()

    override suspend fun setDarkModeActive(isActive: Boolean) =
        settingPreferences.setDarkModeActive(isActive)
}
