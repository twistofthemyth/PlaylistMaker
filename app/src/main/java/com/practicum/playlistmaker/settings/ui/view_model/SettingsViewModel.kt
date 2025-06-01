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

    fun setTheme(theme: AppStyle) {
        if (state.value?.theme != theme) {
            settingsInteractor.changeAppTheme(theme)
            state.value?.theme = theme
            state.postValue(state.value)
        }
    }

    private fun getDefaultSettings(): SettingsState {
        return SettingsState(settingsInteractor.getAppTheme())
    }

    data class SettingsState(var theme: AppStyle);
}