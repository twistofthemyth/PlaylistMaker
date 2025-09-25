package com.practicum.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.PlaylistRepository
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.ui_utils.TrackNavigatableViewModel
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class MediaViewModel(val playlistRepository: PlaylistRepository) :
    TrackNavigatableViewModel() {
    private val favoritesState: MutableLiveData<FavoritesState>
    private val playlistsState: MutableLiveData<PlaylistState>

    fun getFavoritesState() = favoritesState as LiveData<FavoritesState>
    fun getPlaylistState() = playlistsState as LiveData<PlaylistState>

    init {
        favoritesState = MutableLiveData(FavoritesState.Empty)
        playlistsState = MutableLiveData(PlaylistState.Loading)
        updateFavoritePlayList()
        updatePlaylist()
    }

    fun updateFavoritePlayList() {
        viewModelScope.launch {
            favoritesState.postValue(FavoritesState.Empty)
            val trackList = playlistRepository.getFavoritesTrack().toList()
            if (trackList.isEmpty()) {
                favoritesState.postValue(FavoritesState.Empty)
            } else {
                favoritesState.postValue(FavoritesState.Content(trackList))
            }
        }
    }

    fun createPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistRepository.addPlaylist(playlist)
            runPlaylistUpdate()
        }
    }

    fun updatePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistRepository.updatePlaylist(playlist)
            runPlaylistUpdate()
        }
    }

    fun deletePlaylist(playlistId: Long) {
        viewModelScope.launch {
            playlistRepository.removePlaylist(playlistId)
            runPlaylistUpdate()
        }
    }

    fun updatePlaylist() {
        viewModelScope.launch {
            runPlaylistUpdate()
        }
    }

    private suspend fun runPlaylistUpdate() {
        val playlistList = playlistRepository.getPlaylists()
        if (playlistList.isEmpty()) {
            playlistsState.postValue(PlaylistState.Empty)
        } else {
            playlistsState.postValue(PlaylistState.Content(playlistList))
        }
    }

    sealed interface FavoritesState {
        data class Content(val favoritesTrack: List<Track>) : FavoritesState
        data object Empty : FavoritesState
    }

    sealed interface PlaylistState {
        data object Loading : PlaylistState
        data class Content(val playlists: List<Playlist>) : PlaylistState
        data object Empty : PlaylistState
    }
}