package com.practicum.playlistmaker.player.service

data class MusicPlayerState(val position: String, val isEnded: Boolean) {
    companion object {
        val DEFAULT_STATE = MusicPlayerState("", true)
    }
}