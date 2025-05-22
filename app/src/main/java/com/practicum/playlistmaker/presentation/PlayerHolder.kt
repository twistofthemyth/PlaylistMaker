package com.practicum.playlistmaker.presentation

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Locale

abstract class PlayerHolder(
    private val url: String,
    private val positionView: TextView
) {

    enum class PlayerState {
        STATE_DEFAULT,
        STATE_PREPARED,
        STATE_PLAYING,
        STATE_PAUSED
    }

    companion object {
        private const val TIME_FORMAT = "mm:ss"
    }

    private var playerState = PlayerState.STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()

    abstract fun onPause()

    abstract fun onPlay()

    abstract fun onComplete()

    abstract fun onPrepare()

    fun preparePlayer() {
        positionView.text = getStartPosition()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            onPrepare()
            playerState = PlayerState.STATE_PREPARED
        }

        mediaPlayer.setOnCompletionListener {
            onComplete()
            playerState = PlayerState.STATE_PREPARED
            positionView.text = getStartPosition()
        }
    }

    fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
        startCurrentPositionUpdater()
        onPlay()
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
        onPause()
    }

    fun release() {
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
        return SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(getCurrentPosition())
    }

    fun getStartPosition(): String {
        return SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(0)
    }

    fun startCurrentPositionUpdater() {
        val handler = Handler.createAsync(Looper.getMainLooper())
        val updateCurrentPositionTask = object : Runnable {
            override fun run() {
                if (playerState == PlayerState.STATE_PLAYING) {
                    positionView.text = getFormattedCurrentPosition()
                    handler.postDelayed(this, 100)
                } else {
                    handler.removeCallbacks(this)
                }
            }
        }
        handler.post(updateCurrentPositionTask)
    }
}