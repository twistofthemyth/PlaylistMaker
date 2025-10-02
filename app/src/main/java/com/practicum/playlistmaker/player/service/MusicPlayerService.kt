package com.practicum.playlistmaker.player.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.practicum.playlistmaker.player.domain.PlayerState
import com.practicum.playlistmaker.player.domain.api.TrackPlayer
import com.practicum.playlistmaker.player.service.MusicPlayerState.Companion.DEFAULT_STATE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MusicPlayerService() : Service(), KoinComponent, IMusicPlayerService {

    companion object {
        const val TRACK_URL_TAG = "track_url"
        private const val TRACK_UPDATE_DELAY = 300L
    }

    private val trackPlayer: TrackPlayer by inject()
    private val binder = MusicServiceBinder()
    private var timerJob: Job? = null
    private val _playerState = MutableStateFlow(DEFAULT_STATE)


    override fun onBind(intent: Intent?): IBinder? {
        intent?.getStringExtra(TRACK_URL_TAG)?.let { trackUrl ->
            trackPlayer.preparePlayer(trackUrl)
            _playerState.value = MusicPlayerState(trackPlayer.getPosition(), true)
        }

        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        trackPlayer.releasePlayer()
        return super.onUnbind(intent)
    }

    override fun startPlayer() {
        trackPlayer.startPlayer()
        startTimer()
    }

    override fun stopPlayer() {
        trackPlayer.stopPlayer()
        timerJob?.cancel()
    }

    override fun state(): StateFlow<MusicPlayerState> {
        return _playerState.asStateFlow()
    }

    private fun startTimer() {
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (trackPlayer.getState() == PlayerState.STATE_PLAYING) {
                delay(TRACK_UPDATE_DELAY)
                _playerState.value = MusicPlayerState(trackPlayer.getPosition(), false)
            }
            _playerState.value = MusicPlayerState(trackPlayer.getPosition(), true)
        }
    }

    inner class MusicServiceBinder : Binder() {
        fun getService(): MusicPlayerService = this@MusicPlayerService
    }
}