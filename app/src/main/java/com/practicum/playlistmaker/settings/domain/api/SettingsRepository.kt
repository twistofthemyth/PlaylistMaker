package com.practicum.playlistmaker.settings.domain.api

import com.practicum.playlistmaker.settings.domain.models.AppStyle

interface SettingsRepository {
    fun getAppTheme(): AppStyle
    fun setAppTheme(theme: AppStyle)
}