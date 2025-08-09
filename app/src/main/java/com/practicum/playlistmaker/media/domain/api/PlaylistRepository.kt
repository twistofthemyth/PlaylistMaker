package com.practicum.playlistmaker.media.domain.api

import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.domain_utils.Resource
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun isTrackInFavorites(trackId: Long): Boolean
    suspend fun isTrackInPlaylist(playlistId: Long, trackId: Long): Boolean
    fun getFavoritesTrack(): Flow<Track>
    suspend fun addPlaylist(playlist: Playlist): List<Playlist>
    suspend fun removePlaylist(playlistId: Long)
    suspend fun getPlaylists(): List<Playlist>
    fun getTracksInPlaylist(playlistId: Long): Flow<Track>
    suspend fun addTrackToPlaylist(playlistId: Long, track: Track): Boolean
    suspend fun addTrackToFavorites(track: Track)
    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long)
    suspend fun removeTrackFromFavorites(trackId: Long)
}