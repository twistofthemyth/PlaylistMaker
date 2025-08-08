package com.practicum.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.domain.api.PlaylistRepository
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.player.domain.PlayerState
import com.practicum.playlistmaker.player.domain.api.TrackPlayer
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.domain_utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class TrackViewModel(
    private val trackId: String,
    private val searchInteractor: SearchInteractor,
    private val playlistRepository: PlaylistRepository,
    private val trackPlayer: TrackPlayer
) : ViewModel(), KoinComponent {

    private val screenState = MutableLiveData<ScreenState>()
    private lateinit var track: Track
    private var timerJob: Job? = null
    private var cachedFavoriteState: Boolean? = null
    private var cachedPlaylists: List<Playlist> = mutableListOf()

    init {
        screenState.postValue(ScreenState.Loading())
        viewModelScope.launch {
            searchInteractor.searchTracks(trackId).collect {
                val initState = when (it) {
                    is Resource.Success<*> -> {
                        if (it.data != null && it.data.isNotEmpty()) {
                            track = it.data[0]
                            trackPlayer.preparePlayer(track)
                            cachedFavoriteState = playlistRepository.isTrackInFavorites(track)
                            cachedPlaylists = playlistRepository.getPlaylists().toList()
                            contentState()
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
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (trackPlayer.getState() == PlayerState.STATE_PLAYING) {
                delay(TRACK_UPDATE_DELAY)
                screenState.postValue(contentState())
            }
            screenState.postValue(contentState())
        }
    }

    fun stopPlayer() {
        trackPlayer.stopPlayer()
        timerJob?.cancel()
        screenState.postValue(contentState())
    }

    fun addTrackToFavorite() {
        cachedFavoriteState = true
        screenState.postValue(contentState())
        viewModelScope.launch {
            playlistRepository.addTrackToFavorites(track)
        }
    }

    fun removeTrackFromFavorite() {
        cachedFavoriteState = false
        screenState.postValue(contentState())
        viewModelScope.launch {
            playlistRepository.removeTrackFromFavorites(track)
        }
    }

    fun toggleTrackFavorites() {
        if (isTrackInFavorites()) {
            removeTrackFromFavorite()
        } else {
            addTrackToFavorite()
        }
    }

    fun addTrackToPlaylist(playlist: Playlist): Boolean {
        if (!playlist.track.contains(track)) {
            viewModelScope.launch {
                playlistRepository.addTrackToPlaylist(playlist, track)
                cachedPlaylists = playlistRepository.getPlaylists().toList()
                screenState.postValue(contentState())
            }
            return true
        } else {
            return false
        }
    }

    private fun isTrackInFavorites(): Boolean {
        return cachedFavoriteState ?: false
    }

    private fun contentState(): ScreenState {
        return ScreenState.Content(
            track,
            if (trackPlayer.getState() == PlayerState.STATE_PLAYING) R.drawable.button_pause_track else R.drawable.button_play_track,
            trackPlayer.getPosition(),
            isTrackInFavorites(),
            cachedPlaylists
        )
    }

    sealed interface ScreenState {
        class Loading() : ScreenState
        data class Content(
            val track: Track,
            val iconResId: Int,
            val position: String,
            var isFavorite: Boolean,
            val playlists: List<Playlist>
        ) : ScreenState

        class Error() : ScreenState
    }

    companion object {
        private const val TRACK_UPDATE_DELAY = 300L
    }
}