package com.practicum.playlistmaker.playlist.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.PlaylistRepository
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.util.domain_utils.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class BottomSheetPlaylistOptionsViewModel(
    val playlistRepository: PlaylistRepository,
    val playlistId: Long
) :
    ViewModel() {

    private val state = MutableLiveData<BottomSheetPlaylistOptionsState>()

    fun getState(): LiveData<BottomSheetPlaylistOptionsState> {
        return state as LiveData<BottomSheetPlaylistOptionsState>
    }

    init {
        viewModelScope.launch {
            val resource = playlistRepository.getPlaylist(playlistId)
            when (resource) {
                is Resource.ClientError<*> -> state.postValue(BottomSheetPlaylistOptionsState.Error)
                is Resource.ServerError<*> -> state.postValue(BottomSheetPlaylistOptionsState.Error)
                is Resource.Success<*> -> state.postValue(
                    BottomSheetPlaylistOptionsState.Content(
                        resource.data!!
                    )
                )
            }
        }
    }

    fun deletePlaylist() {
        viewModelScope.async {
            playlistRepository.removePlaylist(playlistId)
        }
    }


    sealed interface BottomSheetPlaylistOptionsState {
        data object Error : BottomSheetPlaylistOptionsState
        data class Content(val playlist: Playlist) : BottomSheetPlaylistOptionsState
    }
}