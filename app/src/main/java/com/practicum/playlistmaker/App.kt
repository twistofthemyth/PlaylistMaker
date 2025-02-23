package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val darkTheme = getSharedPreferences().getBoolean(DARK_THEME_KEY, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        getSharedPreferences().edit().putBoolean(DARK_THEME_KEY, darkThemeEnabled).apply()
    }

    fun getSharedPreferences() : SharedPreferences {
        return getSharedPreferences(SHARED_PREFERENCE_KEY, MODE_PRIVATE);
    }

    companion object {
        private const val SHARED_PREFERENCE_KEY = "PlaylistMaker"
        private const val DARK_THEME_KEY = "darkTheme"
    }
}