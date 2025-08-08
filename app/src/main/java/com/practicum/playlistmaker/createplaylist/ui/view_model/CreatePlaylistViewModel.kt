package com.practicum.playlistmaker.createplaylist.ui.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.PlaylistRepository
import com.practicum.playlistmaker.media.domain.models.Playlist
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(private val playlistRepository: PlaylistRepository) : ViewModel() {

    private val state: MutableLiveData<CreatePlaylistState>
    private var name: String
    private var description: String
    private var image: Uri?

    init {
        state = MutableLiveData(CreatePlaylistState.Empty())
        name = ""
        description = ""
        image = null
    }

    fun getState() = state as LiveData<CreatePlaylistState>

    fun setName(value: String) {
        name = value
        postAfterEditState()
    }

    fun setDescription(value: String) {
        description = value
        postAfterEditState()
    }

    fun setImage(value: Uri) {
        image = value
        postAfterEditState()
    }

    private fun postAfterEditState() {
        if (name.isNotEmpty()) {
            state.postValue(CreatePlaylistState.ReadyForCreate())
        } else {
            state.postValue(CreatePlaylistState.InEdit())
        }
    }

    fun createPlaylist() {
        viewModelScope.launch {
            playlistRepository.addPlaylist(
                Playlist(
                    0,
                    name,
                    description,
                    image.toString(),
                    mutableListOf()
                )
            )
        }
        state.postValue(CreatePlaylistState.Created())
    }

    sealed interface CreatePlaylistState {
        class Empty : CreatePlaylistState
        class InEdit : CreatePlaylistState
        class ReadyForCreate : CreatePlaylistState
        class Created : CreatePlaylistState
    }
}