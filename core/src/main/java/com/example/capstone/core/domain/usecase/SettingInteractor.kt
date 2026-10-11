package com.example.capstone.core.domain.usecase

import com.example.capstone.core.domain.repository.ISettingRepository

class SettingInteractor(private val settingRepository: ISettingRepository) : SettingUseCase {

    override fun isDarkModeActive() = settingRepository.isDarkModeActive()

    override suspend fun setDarkModeActive(isActive: Boolean) =
        settingRepository.setDarkModeActive(isActive)
}
