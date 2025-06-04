package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.player.domain.PlayerState
import com.practicum.playlistmaker.search.domain.models.Track
import java.util.function.Consumer

interface TrackPlayer {
    fun getState(): PlayerState
    fun preparePlayer(track: Track)
    fun startPlayer()
    fun stopPlayer()
    fun togglePlayer()
    fun releasePlayer()
    fun getPosition(): String

    fun setOnPositionChangedListener(listener: Consumer<TrackPlayer>)
    fun setOnCompleteListener(listener: Consumer<TrackPlayer>)
}