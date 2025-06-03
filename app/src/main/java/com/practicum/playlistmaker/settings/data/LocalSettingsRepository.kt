package com.practicum.playlistmaker.settings.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.settings.data.dto.SettingsDto
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository
import com.practicum.playlistmaker.settings.domain.models.AppStyle
import java.lang.reflect.Type

class LocalSettingsRepository(private val sharedPreferences: SharedPreferences) : SettingsRepository {

    private val cachedSettings = loadSettings()

    override fun getAppTheme(): AppStyle {
        return cachedSettings.style
    }

    override fun setAppTheme(theme: AppStyle) {
        cachedSettings.style = theme
        saveSettings(cachedSettings)
    }

    private fun loadSettings(): SettingsDto {
        val json = sharedPreferences.getString(SETTINGS_PREF_KEY, null)
        return if (!json.isNullOrEmpty()) {
            val type: Type = object : TypeToken<SettingsDto>() {}.type
            gson.fromJson(json, type)
        } else {
            SettingsDto()
        }
    }

    private fun saveSettings(settings: SettingsDto) {
        sharedPreferences.edit {
            putString(SETTINGS_PREF_KEY, gson.toJson(settings))
        }
    }


    companion object {
        val gson = Gson()
        const val SETTINGS_PREF_KEY = "user_settings"
    }
}