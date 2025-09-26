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
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class TrackViewModel(
    private val trackId: String,
    private val searchInteractor: SearchInteractor,
    private val playlistRepository: PlaylistRepository,
    private val trackPlayer: TrackPlayer
) : ViewModel(), KoinComponent {

    private val trackState = MutableLiveData<TrackState>()
    private val playlistsState = MutableLiveData<PlaylistsState>()
    private lateinit var track: Track
    private var timerJob: Job? = null
    private var cachedFavoriteState: Boolean? = null

    init {
        trackState.postValue(TrackState.Loading)
        viewModelScope.launch {
            async {
                postPlaylistsContent()
            }

            searchInteractor.searchTrackById(trackId).let {
                if (it.data != null) {
                    track = it.data
                    trackPlayer.preparePlayer(track)
                    cachedFavoriteState =
                        playlistRepository.isTrackInFavorites(track.trackId.toLong())
                    postTrackContent()
                } else {
                    trackState.postValue(TrackState.Error)
                }
            }
        }
    }

    fun getScreenState(): LiveData<TrackState> = trackState

    fun getPlaylistsState(): LiveData<PlaylistsState> = playlistsState

    fun releasePlayer() {
        trackPlayer.releasePlayer()
    }

    fun startPlayer() {
        trackPlayer.startPlayer()
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (trackPlayer.getState() == PlayerState.STATE_PLAYING) {
                delay(TRACK_UPDATE_DELAY)
                postTrackContent()
            }
            postTrackContent(true)
        }
    }

    fun stopPlayer() {
        trackPlayer.stopPlayer()
        timerJob?.cancel()
        postTrackContent()
    }

    fun addTrackToFavorite() {
        cachedFavoriteState = true
        postTrackContent()
        viewModelScope.launch {
            playlistRepository.addTrackToFavorites(track)
        }
    }

    fun removeTrackFromFavorite() {
        cachedFavoriteState = false
        postTrackContent()
        viewModelScope.launch {
            playlistRepository.removeTrackFromFavorites(track.trackId.toLong())
        }
    }

    fun toggleTrackFavorites() {
        if (isTrackInFavorites()) {
            removeTrackFromFavorite()
        } else {
            addTrackToFavorite()
        }
    }

    suspend fun addTrackToPlaylist(playlist: Playlist): Boolean {
        val result = playlistRepository.addTrackToPlaylist(playlist.id, track)
        if (result) {
            postPlaylistsContent()
        }
        return result
    }

    private fun isTrackInFavorites(): Boolean {
        return cachedFavoriteState ?: false
    }

    private fun postTrackContent(isEnded: Boolean = false) {
        trackState.postValue(
            TrackState.Content(
                track,
                trackPlayer.getPosition(),
                isTrackInFavorites(),
                isEnded
            )
        )
    }

    private suspend fun postPlaylistsContent() {
        playlistsState.postValue(PlaylistsState.Content(playlistRepository.getPlaylists()))
    }

    sealed interface TrackState {
        data object Loading : TrackState
        data class Content(
            val track: Track,
            val position: String,
            var isFavorite: Boolean,
            var isEnded: Boolean
        ) : TrackState

        data object Error : TrackState
    }

    sealed interface PlaylistsState {
        data object Loading : PlaylistsState
        data class Content(val playlists: List<Playlist>) : PlaylistsState
    }

    companion object {
        private const val TRACK_UPDATE_DELAY = 300L
    }
}