package com.practicum.playlistmaker.createplaylist.ui.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CreatePlaylistViewModel : ViewModel() {

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
        if (image != null) {
            state.postValue(CreatePlaylistState.Completed())
        }
    }

    fun setDescription(value: String) {
        description = value

    }

    fun setImage(value: Uri) {
        image = value
        if (name.isNotEmpty()) {
            state.postValue(CreatePlaylistState.Completed())
        }
    }


    fun createPlaylist() {

    }

    sealed interface CreatePlaylistState {
        class Empty : CreatePlaylistState
        class InEdit : CreatePlaylistState
        class Completed : CreatePlaylistState
    }
}