package com.practicum.playlistmaker.player.domain

import java.util.function.Consumer
import javax.net.ssl.HandshakeCompletedListener

interface TrackPlayer {
    fun getState(): PlayerState
    fun preparePlayer()
    fun startPlayer()
    fun stopPlayer()
    fun togglePlayer()
    fun releasePlayer()
    fun getPosition(): String

    fun setOnPositionChangedListener(listener: Consumer<TrackPlayer>)
    fun setOnCompleteListener(listener: Consumer<TrackPlayer>)
}