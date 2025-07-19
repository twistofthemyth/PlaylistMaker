package com.practicum.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.domain.PlayerState
import com.practicum.playlistmaker.player.domain.api.TrackPlayer
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.domain_utils.Resource
import org.koin.core.component.KoinComponent
import java.util.function.Consumer

class TrackViewModel(
    trackId: String,
    private val searchInteractor: SearchInteractor,
    private val trackPlayer: TrackPlayer
) : ViewModel(), KoinComponent {

    private val screenState = MutableLiveData<ScreenState>()
    private lateinit var track: Track

    init {
        trackPlayer.setOnPositionChangedListener(getPositionChangedListener())
        trackPlayer.setOnCompleteListener(getCompleteListener())
        initState(trackId)
    }

    fun getScreenState(): LiveData<ScreenState> = screenState

    fun releasePlayer() {
        trackPlayer.releasePlayer()
    }

    fun togglePlayer() {
        trackPlayer.togglePlayer()
        if (trackPlayer.getState() == PlayerState.STATE_PAUSED) {
            screenState.postValue(
                ScreenState.Content(
                    track,
                    R.drawable.button_play_track,
                    trackPlayer.getPosition()
                )
            )
        }
    }

    fun pausePlayer() {
        trackPlayer.stopPlayer()
    }

    private fun getPositionChangedListener(): Consumer<TrackPlayer> {
        return Consumer<TrackPlayer> {
            screenState.postValue(
                ScreenState.Content(
                    track,
                    R.drawable.button_pause_track,
                    it.getPosition()
                )
            )
        }
    }

    private fun getCompleteListener(): Consumer<TrackPlayer> {
        return Consumer<TrackPlayer> {
            screenState.postValue(
                ScreenState.Content(
                    track,
                    R.drawable.button_play_track,
                    it.getPosition()
                )
            )
        }
    }

    private fun initState(trackId: String) {
        screenState.postValue(ScreenState.Loading())
        searchInteractor.searchTracks(trackId) {
            val initState = when (it) {
                is Resource.Success<*> -> {
                    if (it.data != null && it.data.isNotEmpty()) {
                        track = it.data[0]
                        trackPlayer.preparePlayer(track)
                        ScreenState.Content(
                            track,
                            R.drawable.button_play_track,
                            trackPlayer.getPosition()
                        )
                    } else {
                        ScreenState.Error()
                    }
                }

                else -> ScreenState.Error()
            }
            screenState.postValue(initState)
        }
    }

    sealed interface ScreenState {
        class Loading() : ScreenState
        data class Content(val track: Track, val iconResId: Int, val position: String) : ScreenState
        class Error() : ScreenState
    }
}