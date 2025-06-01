package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.player.domain.PlayerState
import com.practicum.playlistmaker.player.domain.TrackPlayer
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.function.Consumer

class TrackPlayerImpl(
    private val url: String,
    private val positionConsumer: Consumer<String>
) : TrackPlayer {

    private val mediaPlayer = MediaPlayer()
    private val timeFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    private val updateHandler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            if (playerState == PlayerState.STATE_PLAYING) {
                positionConsumer.accept(getPosition())
                updateHandler.postDelayed(this, 300)
            } else {
                updateHandler.removeCallbacks(this)
            }
        }
    }

    private var playerState = PlayerState.STATE_DEFAULT

    override fun getState() = playerState

    override fun preparePlayer() {
        positionConsumer.accept(getStartPosition())
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.STATE_PREPARED
        }

        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.STATE_PREPARED
            positionConsumer.accept(getStartPosition())
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
        startCurrentPositionUpdater()
    }

    override fun stopPlayer() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
    }

    override fun togglePlayer() {
        when (playerState) {
            PlayerState.STATE_PLAYING -> {
                stopPlayer()
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED, PlayerState.STATE_DEFAULT -> {
                startPlayer()
            }
        }
    }

    override fun releasePlayer() {
        updateHandler.removeCallbacks(updateRunnable)
        mediaPlayer.release()
    }

    override fun getPosition(): String {
        return timeFormat.format(getCurrentPosition())
    }

    private fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    private fun getStartPosition(): String {
        return timeFormat.format(0)
    }

    private fun startCurrentPositionUpdater() {
        updateHandler.post(updateRunnable)
    }
}