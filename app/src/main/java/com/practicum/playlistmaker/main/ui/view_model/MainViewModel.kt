package com.practicum.playlistmaker.main.ui.view_model

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.settings.domain.models.AppStyle
import com.practicum.playlistmaker.util.Creator
import com.practicum.playlistmaker.util.event.Event

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsInteractor = Creator.provideSettingsInteractor(
        application.getSharedPreferences(
            "SettingsViewMode",
            MODE_PRIVATE
        )
    )
    private val screenState = MutableLiveData(setUpDefaultState())
    private val navigationEvent = MutableLiveData<Event<NavigationDestination>>()

    fun getState(): LiveData<ScreenState> = screenState
    fun getNavigationEvent(): LiveData<Event<NavigationDestination>> = navigationEvent

    fun navigateTo(destination: NavigationDestination) {
        navigationEvent.value = Event(destination)
    }

    private fun setUpDefaultState(): ScreenState {
        val theme = settingsInteractor.getAppTheme()
        return ScreenState(theme)
    }


    data class ScreenState(val theme: AppStyle)

    sealed class NavigationDestination {
        object Search : NavigationDestination()
        object Media : NavigationDestination()
        object Settings : NavigationDestination()
    }
}