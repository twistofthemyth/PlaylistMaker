package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.models.AppStyle
import com.practicum.playlistmaker.util.Creator

class App : Application() {

    private lateinit var settingsInteractor: SettingsInteractor

    override fun onCreate() {
        super.onCreate()

        Creator.init(this)

        settingsInteractor = Creator.provideSettingsInteractor()
        when (settingsInteractor.getAppTheme()) {
            AppStyle.LIGHT -> setLightTheme()
            AppStyle.DARK -> setDarkTheme()
        }
    }

    fun setDarkTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    fun setLightTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}