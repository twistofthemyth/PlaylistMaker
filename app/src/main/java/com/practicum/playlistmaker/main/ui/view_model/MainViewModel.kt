package com.practicum.playlistmaker.main.ui.view_model

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.settings.domain.models.AppStyle
import com.practicum.playlistmaker.util.Creator

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsInteractor = Creator.provideSettingsInteractor(
        application.getSharedPreferences(
            "SettingsViewMode",
            MODE_PRIVATE
        )
    )

    private var state = MutableLiveData(setUpDefaultState())

    fun getState(): LiveData<MainState> = state

    private fun setUpDefaultState(): MainState {
        val theme = settingsInteractor.getAppTheme()
        return MainState(theme)
    }


    class MainState(val theme: AppStyle) {

    }
}