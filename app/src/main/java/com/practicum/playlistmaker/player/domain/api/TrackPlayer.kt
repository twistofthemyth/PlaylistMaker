package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.player.domain.PlayerState
import com.practicum.playlistmaker.search.domain.models.Track
import java.util.function.Consumer

interface TrackPlayer {
    fun getState(): PlayerState
    fun preparePlayer(track: Track)
    fun startPlayer()
    fun stopPlayer()
    fun releasePlayer()
    fun getPosition(): String
}