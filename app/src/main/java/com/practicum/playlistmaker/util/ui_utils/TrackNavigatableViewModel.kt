package com.practicum.playlistmaker.util.ui_utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.event.Event

abstract class TrackNavigatableViewModel : ClickDebounceViewModel() {
    private val navigationEvent = MutableLiveData<Event<NavigationDestination>>()

    fun getNavigationEvent(): LiveData<Event<NavigationDestination>> = navigationEvent

    fun clickTrack(track: Track) {
        if (isClickActionAllowed()) {
            navigationEvent.value = Event(NavigationDestination.ToTrack(track))
            onTrackClicked(track)
        }
    }

    open fun onTrackClicked(track: Track) {
        //do nothing by default
    }

    sealed interface NavigationDestination {
        class ToTrack(val track: Track) : NavigationDestination
    }
}