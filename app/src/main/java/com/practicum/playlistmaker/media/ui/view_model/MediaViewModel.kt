package com.practicum.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.FavoriteTracksRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.ui_utils.TrackNavigatableViewModel
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class MediaViewModel(val favoritesTrackRepository: FavoriteTracksRepository) :
    TrackNavigatableViewModel() {

    private val favoritesState: MutableLiveData<FavoritesState>
    private val playlistsState: MutableLiveData<PlaylistState>

    fun getFavoritesState() = favoritesState as LiveData<FavoritesState>
    fun getPlaylistState() = playlistsState as LiveData<PlaylistState>

    init {
        favoritesState = MutableLiveData(FavoritesState.Empty())
        playlistsState = MutableLiveData(PlaylistState.Empty())
        updateTrackList()
    }

    fun updateTrackList() {
        viewModelScope.launch {
            favoritesState.postValue(FavoritesState.Empty())
            val trackList = favoritesTrackRepository.getFavoritesTrack().toList()
            if (trackList.isNotEmpty()) {
                favoritesState.postValue(FavoritesState.Content(trackList))
            }
        }
    }

    sealed interface FavoritesState {
        class Content(val favoritesTrack: List<Track>) : FavoritesState
        class Empty() : FavoritesState
    }

    sealed interface PlaylistState {
        class Empty() : PlaylistState
    }
}