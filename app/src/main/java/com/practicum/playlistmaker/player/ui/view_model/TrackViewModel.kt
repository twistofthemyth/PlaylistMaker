package com.practicum.playlistmaker.player.ui.view_model

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.domain.api.PlaylistRepository
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.player.service.MusicPlayerListener
import com.practicum.playlistmaker.player.service.MusicPlayerService
import com.practicum.playlistmaker.player.service.MusicPlayerService.Companion.TRACK_ARTIST_TAG
import com.practicum.playlistmaker.player.service.MusicPlayerService.Companion.TRACK_NAME_TAG
import com.practicum.playlistmaker.player.service.MusicPlayerService.Companion.TRACK_URL_TAG
import com.practicum.playlistmaker.player.service.MusicPlayerState
import com.practicum.playlistmaker.player.service.MusicPlayerState.Companion.DEFAULT_STATE
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class TrackViewModel(
    private val trackId: String,
    private val searchInteractor: SearchInteractor,
    private val playlistRepository: PlaylistRepository,
) : ViewModel(), KoinComponent {
    private val trackState = MutableLiveData<TrackState>()
    private val playlistsState = MutableLiveData<PlaylistsState>()
    private val _musicPlayerState = MutableStateFlow(DEFAULT_STATE)
    private var boundService: MusicPlayerService? = null
    private var isServiceBounded = false
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MusicPlayerService.MusicServiceBinder
            boundService = binder.getService()
            isServiceBounded = true
            boundService?.setListener(musicPlayerListener)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            boundService = null
            isServiceBounded = false
        }
    }
    private val musicPlayerListener = object : MusicPlayerListener {
        override fun onStateChanged(state: MusicPlayerState) {
            _musicPlayerState.value = state
        }
    }

    private lateinit var track: Track

    init {
        trackState.postValue(TrackState.Loading)
    }

    fun getScreenState(): LiveData<TrackState> = trackState

    fun getPlaylistsState(): LiveData<PlaylistsState> = playlistsState

    fun getPlayerState() = _musicPlayerState.asStateFlow()

    fun bindService(context: Context) {
        viewModelScope.launch {
            async { playlistsState.postValue(PlaylistsState.Content(playlistRepository.getPlaylists())) }
            val resource = searchInteractor.searchTrackById(trackId)
            if (resource.data != null) {
                track = resource.data
                bindService(context, track.previewUrl, track.artistName, track.trackName)
                trackState.postValue(TrackState.Content(track, isTrackInFavorites()))
            } else {
                trackState.postValue(TrackState.Error)
            }
        }
    }

    fun unbindService(context: Context) {
        if (isServiceBounded) {
            context.unbindService(connection)
            isServiceBounded = false
        }
    }

    fun play() {
        if (!isServiceBounded) trackState.postValue(TrackState.Error)
        boundService?.startPlayer()
    }

    fun pause() {
        if (!isServiceBounded) trackState.postValue(TrackState.Error)
        boundService?.stopPlayer()
    }

    fun collapse() {
        boundService?.startForeground()
    }

    fun expand() {
        boundService?.stopForeground()
    }

    suspend fun toggleTrackFavorites() {
        if (isTrackInFavorites()) {
            removeTrackFromFavorite()
        } else {
            addTrackToFavorite()
        }
    }

    suspend fun addTrackToFavorite() {
        playlistRepository.addTrackToFavorites(track)
        trackState.postValue(TrackState.Content(track, true))
    }

    suspend fun removeTrackFromFavorite() {
        playlistRepository.removeTrackFromFavorites(track.trackId.toLong())
        trackState.postValue(TrackState.Content(track, false))
    }

    suspend fun addTrackToPlaylist(playlist: Playlist): Boolean {
        val isSuccessfullyAdded = playlistRepository.addTrackToPlaylist(playlist.id, track)
        if (isSuccessfullyAdded) playlistsState.postValue(PlaylistsState.Content(playlistRepository.getPlaylists()))
        return isSuccessfullyAdded
    }

    private fun bindService(context: Context, url: String, artistName: String, trackName: String) {
        val intent = Intent(context, MusicPlayerService::class.java)
            .putExtra(TRACK_URL_TAG, url)
            .putExtra(TRACK_NAME_TAG, trackName)
            .putExtra(TRACK_ARTIST_TAG, artistName)
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    private suspend fun isTrackInFavorites(): Boolean {
        return playlistRepository.isTrackInFavorites(track.trackId.toLong())
    }

    sealed interface TrackState {
        data object Loading : TrackState
        data class Content(val track: Track, var isFavorite: Boolean) : TrackState
        data object Error : TrackState
    }

    sealed interface PlaylistsState {
        data object Loading : PlaylistsState
        data class Content(val playlists: List<Playlist>) : PlaylistsState
    }
}