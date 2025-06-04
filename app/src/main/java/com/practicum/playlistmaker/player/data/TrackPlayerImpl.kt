package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.player.domain.PlayerState
import com.practicum.playlistmaker.player.domain.api.TrackPlayer
import com.practicum.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.function.Consumer

class TrackPlayerImpl(private val mediaPlayer: MediaPlayer) : TrackPlayer {

    private val timeFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    private val updateHandler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            if (playerState == PlayerState.STATE_PLAYING) {
                onPositionChangedListener?.accept(this@TrackPlayerImpl)
                updateHandler.postDelayed(this, 300)
            } else {
                updateHandler.removeCallbacks(this)
            }
        }
    }

    private var onPositionChangedListener: Consumer<TrackPlayer>? = null
    private var onCompleteListener: Consumer<TrackPlayer>? = null

    private var playerState = PlayerState.STATE_DEFAULT

    override fun getState() = playerState

    override fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.STATE_PREPARED
        }

        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.STATE_PREPARED
            onPositionChangedListener?.accept(this)
            onCompleteListener?.accept(this)
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

    override fun setOnPositionChangedListener(listener: Consumer<TrackPlayer>) {
        onPositionChangedListener = listener
    }

    override fun setOnCompleteListener(listener: Consumer<TrackPlayer>) {
        onCompleteListener = listener
    }

    private fun getCurrentPosition(): Int {
        return when(getState()) {
            PlayerState.STATE_DEFAULT,PlayerState.STATE_PREPARED -> 0
            PlayerState.STATE_PLAYING,PlayerState.STATE_PAUSED  -> mediaPlayer.currentPosition
        }
    }

    private fun startCurrentPositionUpdater() {
        updateHandler.post(updateRunnable)
    }
}