package com.practicum.playlistmaker.createplaylist.ui.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.media.domain.api.PlaylistRepository
import com.practicum.playlistmaker.media.domain.models.Playlist

class CreatePlaylistViewModel(private val playlistRepository: PlaylistRepository) : ViewModel() {

    private val state: MutableLiveData<CreatePlaylistState>
    private var name: String
    private var description: String
    private var image: Uri?

    init {
        state = MutableLiveData(CreatePlaylistState.Empty)
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
        if (name.trim().isNotEmpty()) {
            state.postValue(CreatePlaylistState.ReadyForCreate)
        } else {
            state.postValue(CreatePlaylistState.InEdit)
        }
    }

    fun exitEditor(): Playlist {
        state.postValue(CreatePlaylistState.Created)
        return Playlist(
            0,
            name,
            description,
            image.toString(),
            mutableListOf()
        )
    }

    sealed interface CreatePlaylistState {
        data object Empty : CreatePlaylistState
        data object InEdit : CreatePlaylistState
        data object ReadyForCreate : CreatePlaylistState
        data object Created : CreatePlaylistState
    }
}