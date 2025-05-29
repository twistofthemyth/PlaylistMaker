package com.practicum.playlistmaker.settings.domain.impl

import com.practicum.playlistmaker.settings.domain.api.SettingAppStyleInteractor
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository
import com.practicum.playlistmaker.settings.domain.models.AppStyle

class SettingsInteractorImpl(private val repo: SettingsRepository) : SettingAppStyleInteractor {

    override fun getAppTheme(): AppStyle {
        return repo.getAppTheme()
    }

    override fun changeAppTheme(theme: AppStyle) {
        repo.setAppTheme(theme)
    }

}