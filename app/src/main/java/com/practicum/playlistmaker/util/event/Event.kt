package com.practicum.playlistmaker.util.event

class Event<out T>(private val content: T) {
    private var hasBeenHandled = false

    fun getContent() = content

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }


}