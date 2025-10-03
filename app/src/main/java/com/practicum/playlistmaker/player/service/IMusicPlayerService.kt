package com.practicum.playlistmaker.player.service

import android.content.Context

interface IMusicPlayerService {
    fun startPlayer()
    fun stopPlayer()
    fun setListener(listener: MusicPlayerListener)

    fun startForeground()

    fun stopForeground()
}