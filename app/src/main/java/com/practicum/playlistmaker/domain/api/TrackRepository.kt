package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface TrackRepository {
    fun searchTrack(query: String): List<Track>

    fun addTrackToHistory(track: Track)

    fun clearHistory()

    fun getTrackInHistory(position: Int): Track

    fun getHistorySize(): Int

    fun getAllHistory(): List<Track>
}