package com.practicum.playlistmaker.media.domain.api

import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.domain_utils.Resource
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun isTrackInFavorites(track: Track): Boolean
    suspend fun isTrackInPlaylist(playlist: Playlist, track: Track): Boolean
    fun getFavoritesTrack(): Flow<Track>
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun removePlaylist(playlistId: Long)
    fun getPlaylists(): Flow<Playlist>
    suspend fun getPlaylist(playlistId: Long): Resource<Playlist>
    fun getTracksInPlaylist(playlist: Playlist): Flow<Track>
    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)
    suspend fun addTrackToFavorites(track: Track)
    suspend fun removeTrackFromPlaylist(playlist: Playlist, track: Track)
    suspend fun removeTrackFromFavorites(track: Track)
}