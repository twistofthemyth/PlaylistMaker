package com.practicum.playlistmaker.util

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

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val TIME_FORMAT = "mm:ss"
    }

    private var playerState = STATE_DEFAULT
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
            playerState = STATE_PREPARED
        }

        mediaPlayer.setOnCompletionListener {
            onComplete()
            playerState = STATE_PREPARED
            positionView.text = getStartPosition()
        }
    }

    fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        startCurrentPositionUpdater()
        onPlay()
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        onPause()
    }

    fun release() {
        mediaPlayer.release()
    }

    fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
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
                if (playerState == STATE_PLAYING) {
                    positionView.text = getFormattedCurrentPosition()
                    handler.postDelayed(this, 300)
                } else {
                    handler.removeCallbacks(this)
                }
            }
        }
        handler.post(updateCurrentPositionTask)
    }
}