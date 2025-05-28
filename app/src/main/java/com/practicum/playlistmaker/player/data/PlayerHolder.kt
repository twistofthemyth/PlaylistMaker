package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerHolder(
    private val url: String,
    private val onPositionUpdateAction: (String) -> Unit = {},
    private val onPauseAction: () -> Unit = {},
    private val onPlayAction: () -> Unit = {},
    private val onCompleteAction: () -> Unit = {},
    private val onPrepareAction: () -> Unit = {}
) {

    private enum class PlayerState {
        STATE_DEFAULT,
        STATE_PREPARED,
        STATE_PLAYING,
        STATE_PAUSED
    }

    private var playerState = PlayerState.STATE_DEFAULT
    private val mediaPlayer = MediaPlayer()
    private val timeFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    private val updateHandler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            if (playerState == PlayerState.STATE_PLAYING) {
                onPositionUpdateAction.invoke(getFormattedCurrentPosition())
                updateHandler.postDelayed(this, 300)
            } else {
                updateHandler.removeCallbacks(this)
            }
        }
    }

    fun preparePlayer() {
        onPositionUpdateAction.invoke(getStartPosition())
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            onPrepareAction.invoke()
            playerState = PlayerState.STATE_PREPARED
        }

        mediaPlayer.setOnCompletionListener {
            onCompleteAction.invoke()
            playerState = PlayerState.STATE_PREPARED
            onPositionUpdateAction.invoke(getStartPosition())
        }
    }

    fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
        startCurrentPositionUpdater()
        onPlayAction.invoke()
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
        onPauseAction.invoke()
    }

    fun release() {
        updateHandler.removeCallbacks(updateRunnable)
        mediaPlayer.release()
    }

    fun playbackControl() {
        when (playerState) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED, PlayerState.STATE_DEFAULT -> {
                startPlayer()
            }
        }
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    fun getFormattedCurrentPosition(): String {
        return timeFormat.format(getCurrentPosition())
    }

    fun getStartPosition(): String {
        return timeFormat.format(0)
    }

    fun startCurrentPositionUpdater() {
        updateHandler.post(updateRunnable)
    }
}