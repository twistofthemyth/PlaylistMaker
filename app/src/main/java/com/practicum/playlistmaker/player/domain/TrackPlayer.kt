package com.practicum.playlistmaker.player.domain

interface TrackPlayer {
    fun getState(): PlayerState
    fun preparePlayer()
    fun startPlayer()
    fun stopPlayer()
    fun togglePlayer()
    fun releasePlayer()
    fun getPosition(): String
}