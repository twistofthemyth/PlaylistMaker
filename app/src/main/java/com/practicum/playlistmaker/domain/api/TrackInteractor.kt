package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track
import java.util.function.Consumer

interface TrackInteractor {
    fun searchTracks(query: String, consumer: Consumer<List<Track>>)

    fun addTrackToHistory(track: Track)

    fun clearHistory()

    fun getTrackInHistory(position: Int, consumer: Consumer<Track>)

    fun getHistorySize(consumer: Consumer<Int>)

    fun getAllHistory(consumer: Consumer<List<Track>>)
}