package com.practicum.playlistmaker.player.service

interface MusicPlayerListener {
    fun onStateChanged(state: MusicPlayerState)
}