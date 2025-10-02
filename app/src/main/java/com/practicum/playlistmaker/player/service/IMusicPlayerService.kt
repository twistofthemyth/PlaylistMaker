package com.practicum.playlistmaker.player.service

import kotlinx.coroutines.flow.StateFlow

interface IMusicPlayerService {
    fun startPlayer()
    fun stopPlayer()

    fun state() : StateFlow<MusicPlayerState>
}