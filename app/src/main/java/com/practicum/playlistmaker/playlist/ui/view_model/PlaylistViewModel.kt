package com.practicum.playlistmaker.playlist.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.PlaylistRepository
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.ui_utils.TrackNavigatableViewModel
import kotlinx.coroutines.launch

class PlaylistViewModel(val playlistRepository: PlaylistRepository, val playlistId: Long) :
    TrackNavigatableViewModel() {

    private val state = MutableLiveData<PlaylistState>()

    fun getState(): LiveData<PlaylistState> {
        return state
    }

    init {
        state.postValue(PlaylistState.Loading())
        viewModelScope.launch {
            updateState()
        }
    }

    fun deleteTrackFromPlaylist(track: Track) {
        viewModelScope.launch {
            playlistRepository.removeTrackFromPlaylist(playlistId, track.trackId.toLong())
            updateState()
        }
    }

    private suspend fun updateState() {
        val playlist = playlistRepository.getPlaylists().toList().find { it.id == playlistId }
        if (playlist != null) {
            state.postValue(
                PlaylistState.Content(
                    playlist,
                    playlist.track.size,
                    computeDuration(playlist)
                )
            )
        }
    }

    private fun computeDuration(playlist: Playlist): Int {
        var duration = 0
        playlist.track.forEach {
            val (minutes, seconds) = it.trackTime.split(":").map { time -> time.toInt() }
            duration += (minutes + seconds / 60)
        }
        return duration
    }

    sealed interface PlaylistState {
        class Loading() : PlaylistState
        class Content(val playlist: Playlist, val count: Int, val duration: Int) :
            PlaylistState
    }
}