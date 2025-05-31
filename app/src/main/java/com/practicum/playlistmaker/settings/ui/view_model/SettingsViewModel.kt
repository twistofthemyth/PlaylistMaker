package com.practicum.playlistmaker.settings.ui.view_model

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.settings.domain.models.AppStyle
import com.practicum.playlistmaker.util.Creator

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsInteractor = Creator.provideSettingsInteractor(
        application.getSharedPreferences(
            "SettingsViewMode",
            MODE_PRIVATE
        )
    )

    private val state = MutableLiveData<SettingsState>(getDefaultSettings())


    fun getState(): LiveData<SettingsState> = state

    fun toggleTheme() {
        when (state.value?.theme) {
            AppStyle.LIGHT -> {
                state.value?.theme = AppStyle.DARK
                settingsInteractor.changeAppTheme(AppStyle.DARK)
            }

            AppStyle.DARK -> {
                state.value?.theme = AppStyle.LIGHT
                settingsInteractor.changeAppTheme(AppStyle.LIGHT)
            }

            null -> {
                state.value = getDefaultSettings()
            }
        }
    }

    private fun getDefaultSettings(): SettingsState {
        return SettingsState(settingsInteractor.getAppTheme())
    }

    data class SettingsState(var theme: AppStyle);
}