package com.practicum.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.domain.PlayerState
import com.practicum.playlistmaker.player.domain.api.TrackPlayer
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.domain_utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import java.util.function.Consumer

class TrackViewModel(
    trackId: String,
    searchInteractor: SearchInteractor,
    private val trackPlayer: TrackPlayer
) : ViewModel(), KoinComponent {

    private val screenState = MutableLiveData<ScreenState>()
    private lateinit var track: Track
    private var timerJob: Job? = null

    init {
        screenState.postValue(ScreenState.Loading())
        viewModelScope.launch {
            searchInteractor.searchTracks(trackId).collect {
                val initState = when (it) {
                    is Resource.Success<*> -> {
                        if (it.data != null && it.data.isNotEmpty()) {
                            track = it.data[0]
                            trackPlayer.preparePlayer(track)
                            stoppedState()
                        } else {
                            ScreenState.Error()
                        }
                    }

                    else -> ScreenState.Error()
                }
                screenState.postValue(initState)
            }
        }
    }

    fun getScreenState(): LiveData<ScreenState> = screenState

    fun releasePlayer() {
        trackPlayer.releasePlayer()
    }

    fun togglePlayer() {
        when (trackPlayer.getState()) {
            PlayerState.STATE_PLAYING -> {
                stopPlayer()
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED, PlayerState.STATE_DEFAULT -> {
                startPlayer()
            }
        }
    }

    fun startPlayer() {
        trackPlayer.startPlayer()
        timerJob = viewModelScope.launch {
            while (trackPlayer.getState() == PlayerState.STATE_PLAYING) {
                delay(300L)
                screenState.postValue(playingState())
            }
            screenState.postValue(stoppedState())
        }
    }

    fun stopPlayer() {
        trackPlayer.stopPlayer()
        timerJob?.cancel()
        screenState.postValue(stoppedState())
    }

    private fun playingState(): ScreenState {
        return ScreenState.Content(
            track,
            R.drawable.button_pause_track,
            trackPlayer.getPosition()
        )
    }

    private fun stoppedState(): ScreenState {
        return ScreenState.Content(
            track,
            R.drawable.button_play_track,
            trackPlayer.getPosition()
        )
    }

    sealed interface ScreenState {
        class Loading() : ScreenState
        data class Content(val track: Track, val iconResId: Int, val position: String) : ScreenState
        class Error() : ScreenState
    }
}