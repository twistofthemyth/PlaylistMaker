package com.practicum.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.domain.PlayerState
import com.practicum.playlistmaker.player.domain.TrackPlayer
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.Creator
import java.util.function.Consumer

class TrackViewModel(private val track: Track) : ViewModel() {

    private val screenState = MutableLiveData<ScreenState>()
    private val trackPlayer: TrackPlayer

    init {
        trackPlayer = Creator.provideTrackPlayer(track, getPositionChangedListener(), getCompleteListener())
        trackPlayer.preparePlayer()
        screenState.postValue(ScreenState.Pause(track, R.drawable.button_play_track, trackPlayer.getPosition()))
    }

    fun getNewScreenState(): LiveData<ScreenState> = screenState

    fun releasePlayer() {
        trackPlayer.releasePlayer()
    }

    fun togglePlayer() {
        trackPlayer.togglePlayer()
        if(trackPlayer.getState() == PlayerState.STATE_PAUSED) {
            screenState.postValue(ScreenState.Pause(track, R.drawable.button_play_track, trackPlayer.getPosition()))
        }
    }

    fun pausePlayer() {
        trackPlayer.stopPlayer()
    }

    private fun getPositionChangedListener(): Consumer<TrackPlayer> {
        return Consumer<TrackPlayer> { screenState.postValue(ScreenState.Playing(track, R.drawable.button_pause_track, it.getPosition())) }
    }

    private fun getCompleteListener(): Consumer<TrackPlayer> {
        return Consumer<TrackPlayer> {screenState.postValue(ScreenState.Playing(track, R.drawable.button_play_track, it.getPosition()))}
    }

    sealed interface ScreenState {
        val track: Track
        val iconResId: Int
        val position: String

        data class Playing(override val track: Track, override val iconResId: Int, override val position: String) : ScreenState
        data class Pause(override val track: Track, override val iconResId: Int, override val position: String) : ScreenState
    }

    companion object {
        fun getViewModelFactory(track: Track): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TrackViewModel(track) as T
                }
            }
    }
}