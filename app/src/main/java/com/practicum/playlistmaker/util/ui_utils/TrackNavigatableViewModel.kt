package com.practicum.playlistmaker.util.ui_utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.event.Event

abstract class TrackNavigatableViewModel : ClickDebounceViewModel() {
    private val trackNavigationEvent = MutableLiveData<Event<Track>>()

    fun getTrackNavigationEvent(): LiveData<Event<Track>> = trackNavigationEvent

    fun clickTrack(track: Track) {
        if (isClickActionAllowed()) {
            trackNavigationEvent.value = Event(track)
            onTrackClicked(track)
        }
    }

    open fun onTrackClicked(track: Track) {
        //do nothing by default
    }
}