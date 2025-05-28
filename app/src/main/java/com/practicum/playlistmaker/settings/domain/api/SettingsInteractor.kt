package com.practicum.playlistmaker.settings.domain.api

import com.practicum.playlistmaker.settings.domain.models.AppStyle

interface SettingsInteractor {
    fun changeAppTheme(theme: AppStyle)
}