package com.practicum.playlistmaker.settings.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.models.AppStyle
import com.practicum.playlistmaker.util.event.Event

class SettingsViewModel(
    val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private val screenState = MutableLiveData(getDefaultSettings())
    private val navigationEvent = MutableLiveData<Event<NavigationDestination>>()
    private val changeThemeEvent = MutableLiveData<Event<AppStyle>>()

    fun getScreenState(): LiveData<SettingsState> = screenState

    fun getNavigationEvent(): LiveData<Event<NavigationDestination>> = navigationEvent

    fun getChangeThemeEvent(): LiveData<Event<AppStyle>> = changeThemeEvent

    fun setTheme(theme: AppStyle) {
        if (screenState.value?.theme != theme) {
            settingsInteractor.changeAppTheme(theme)
            screenState.value?.theme = theme
            changeThemeEvent.value = Event(theme)
        }
    }

    fun navigateTo(destination: NavigationDestination) {
        navigationEvent.value = Event(destination)
    }

    private fun getDefaultSettings(): SettingsState {
        return SettingsState(settingsInteractor.getAppTheme())
    }

    data class SettingsState(var theme: AppStyle)

    sealed interface NavigationDestination {
        object Share : NavigationDestination
        object Support : NavigationDestination
        object Agreement : NavigationDestination
    }
}