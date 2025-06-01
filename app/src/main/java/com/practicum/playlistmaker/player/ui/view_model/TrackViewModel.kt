package com.practicum.playlistmaker.player.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.player.domain.PlayerState
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.Creator

class TrackViewModel(track: Track, application: Application) : AndroidViewModel(application) {

    private val screenState = MutableLiveData<TrackScreenState>()
    private val playStatus = MutableLiveData<PlayState>()
    private val trackPlayer = Creator.provideTrackPlayer(track) {
        playStatus.value?.position = it
        playStatus.postValue(playStatus.value)
    }

    init {
        trackPlayer.preparePlayer()
        screenState.postValue(TrackScreenState.Content(track))
        playStatus.postValue(
            PlayState(
                trackPlayer.getState(),
                trackPlayer.getPosition()
            )
        )
    }

    fun getScreenState(): LiveData<TrackScreenState> = screenState
    fun getPlayStatus(): LiveData<PlayState> = playStatus

    fun releasePlayer() {
        trackPlayer.releasePlayer()
        playStatus.value?.playerState = trackPlayer.getState()
        playStatus.postValue(playStatus.value)
    }

    fun togglePlayer() {
        trackPlayer.togglePlayer()
        playStatus.value?.playerState = trackPlayer.getState()
        playStatus.postValue(playStatus.value)
    }

    fun pausePlayer() {
        trackPlayer.stopPlayer()
        playStatus.value?.playerState = trackPlayer.getState()
        playStatus.postValue(playStatus.value)
    }

    sealed class TrackScreenState {
        data class Content(val track: Track) : TrackScreenState()
    }

    class PlayState(var playerState: PlayerState, var position: String)

    companion object {
        fun getViewModelFactory(track: Track, app: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TrackViewModel(track, app) as T
                }
            }
    }
}