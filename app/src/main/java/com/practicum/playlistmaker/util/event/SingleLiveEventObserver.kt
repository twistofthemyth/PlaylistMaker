package com.practicum.playlistmaker.util.event

import androidx.lifecycle.Observer

class SingleLiveEventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {

    override fun onChanged(event: Event<T>) {
        event.getContentIfNotHandled()?.let { value ->
            onEventUnhandledContent(value)
        }
    }
}