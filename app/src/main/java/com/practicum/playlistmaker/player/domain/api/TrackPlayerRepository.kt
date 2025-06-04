package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface TrackPlayerRepository {

    fun getTrackForPlaying() : Track

    fun setTrackForPlaying(track: Track)
}