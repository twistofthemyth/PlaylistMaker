package com.practicum.playlistmaker.playlist_editor.ui.view_model

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.PlaylistRepository
import com.practicum.playlistmaker.media.domain.models.Playlist
import kotlinx.coroutines.launch

class PlaylistEditorViewModel(val playlistRepository: PlaylistRepository, val playlistId: Long) :
    ViewModel() {

    private val state: MutableLiveData<CreatePlaylistState>
    private var name: String = ""
    private var description: String = ""
    private var image: Uri? = null

    init {
        state = MutableLiveData(CreatePlaylistState.InEdit)
        if (playlistId > 0) {
            viewModelScope.launch {
                val resource = playlistRepository.getPlaylist(playlistId)
                if (resource.data != null) {
                    initEditState(resource.data)
                    state.postValue(CreatePlaylistState.InitEdition(resource.data))
                } else {
                    state.postValue(CreatePlaylistState.InitCreation)
                }
            }
        } else {
            state.postValue(CreatePlaylistState.InitCreation)
        }
    }

    private fun initEditState(playlist: Playlist) {
        name = playlist.name
        description = playlist.description
        image = playlist.image?.toUri()
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
        if (name.trim().isNotEmpty()) {
            state.postValue(CreatePlaylistState.ReadyForCreate)
        } else {
            state.postValue(CreatePlaylistState.InEdit)
        }
    }

    fun exitEditor(): Playlist {
        state.postValue(CreatePlaylistState.Created)
        return Playlist(
            if (playlistId < 0) 0 else playlistId,
            name,
            description,
            image.toString(),
            mutableListOf()
        )
    }

    sealed interface CreatePlaylistState {
        data object InitCreation : CreatePlaylistState
        data class InitEdition(val playlist: Playlist) : CreatePlaylistState
        data object InEdit : CreatePlaylistState
        data object ReadyForCreate : CreatePlaylistState
        data object Created : CreatePlaylistState
    }
}