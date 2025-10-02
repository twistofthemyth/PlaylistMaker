package com.practicum.playlistmaker.player.service

interface IMusicPlayerService {
    fun startPlayer()
    fun stopPlayer()
    fun setListener(listener: MusicPlayerListener)
}