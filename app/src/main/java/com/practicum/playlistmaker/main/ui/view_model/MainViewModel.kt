package com.practicum.playlistmaker.main.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.util.event.Event

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val navigationEvent = MutableLiveData<Event<NavigationDestination>>()

    fun getNavigationEvent(): LiveData<Event<NavigationDestination>> = navigationEvent

    fun navigateTo(destination: NavigationDestination) {
        navigationEvent.value = Event(destination)
    }

    sealed interface NavigationDestination {
        object Search : NavigationDestination
        object Media : NavigationDestination
        object Settings : NavigationDestination
    }
}