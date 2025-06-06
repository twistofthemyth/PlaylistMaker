package com.practicum.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MediaViewModel : ViewModel() {

    private val favoritesState: MutableLiveData<FavoritesState>
    private val playlistsState: MutableLiveData<PlaylistState>

    fun getFavoritesState() = favoritesState as LiveData<FavoritesState>
    fun getPlaylistState() = playlistsState as LiveData<PlaylistState>

    init {
        favoritesState = MutableLiveData(FavoritesState.Empty())
        playlistsState = MutableLiveData(PlaylistState.Empty())
    }

    sealed interface FavoritesState {
        class Empty() : FavoritesState
    }

    sealed interface PlaylistState {
        class Empty() : PlaylistState
    }
}