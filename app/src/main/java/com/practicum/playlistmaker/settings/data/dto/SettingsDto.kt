package com.practicum.playlistmaker.settings.data.dto

import com.practicum.playlistmaker.settings.domain.models.AppStyle

data class SettingsDto(
    var style: AppStyle = AppStyle.LIGHT
)