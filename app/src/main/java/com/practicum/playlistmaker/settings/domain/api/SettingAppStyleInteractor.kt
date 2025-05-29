package com.practicum.playlistmaker.settings.domain.api

import com.practicum.playlistmaker.settings.domain.models.AppStyle

interface SettingAppStyleInteractor {
    fun getAppTheme(): AppStyle
    fun changeAppTheme(theme: AppStyle)
}