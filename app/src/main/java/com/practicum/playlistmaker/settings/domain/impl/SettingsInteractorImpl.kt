package com.practicum.playlistmaker.settings.domain.impl

import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository
import com.practicum.playlistmaker.settings.domain.models.AppStyle

class SettingsInteractorImpl(private val repo: SettingsRepository) : SettingsInteractor {

    override fun changeAppTheme(theme: AppStyle) {
        repo.setAppTheme(theme)
    }

}