package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.player.domain.PlayerState
import com.practicum.playlistmaker.player.domain.api.TrackPlayer
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.function.Consumer

class TrackPlayerImpl(private val mediaPlayer: MediaPlayer) : TrackPlayer {

    private val timeFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    private var playerState = PlayerState.STATE_DEFAULT
    override fun getState() = playerState

    override fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.STATE_PREPARED
        }

        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.STATE_PREPARED
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
    }

    override fun stopPlayer() {
        if(playerState == PlayerState.STATE_PLAYING) {
            mediaPlayer.pause()
            playerState = PlayerState.STATE_PAUSED
        }
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun getPosition(): String {
        return timeFormat.format(getCurrentPosition())
    }

    private fun getCurrentPosition(): Int {
        return when(getState()) {
            PlayerState.STATE_DEFAULT,PlayerState.STATE_PREPARED -> 0
            PlayerState.STATE_PLAYING,PlayerState.STATE_PAUSED  -> mediaPlayer.currentPosition
        }
    }
}