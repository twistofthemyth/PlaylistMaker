package com.practicum.playlistmaker.media.data.impl

import com.practicum.playlistmaker.media.data.db.AppDatabase
import com.practicum.playlistmaker.media.data.db.PlaylistDao
import com.practicum.playlistmaker.media.data.db.PlaylistTrackAssociationEntity
import com.practicum.playlistmaker.media.data.db.TrackEntity
import com.practicum.playlistmaker.media.domain.api.PlaylistRepository
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.data_utils.DataConverter
import com.practicum.playlistmaker.util.domain_utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

class PlaylistRepositoryImpl(val appDatabase: AppDatabase) : PlaylistRepository {
    private val favoritesPlaylistId by lazy {
        runBlocking {
            appDatabase.getPlaylistDao()
                .getSystemPlaylist(PlaylistDao.SystemPlaylist.FAVORITES)
                .id
        }
    }

    override suspend fun isTrackInFavorites(trackId: Long): Boolean {
        return appDatabase.getPlaylistDao().isTrackInPlaylist(trackId, favoritesPlaylistId) > 0
    }

    override suspend fun isTrackInPlaylist(playlistId: Long, trackId: Long): Boolean {
        return appDatabase.getPlaylistDao().isTrackInPlaylist(trackId, playlistId) > 0
    }

    override fun getFavoritesTrack(): Flow<Track> {
        return getTracksInPlaylist(favoritesPlaylistId)
    }

    override suspend fun addPlaylist(playlist: Playlist): List<Playlist> {
        appDatabase.getPlaylistDao()
            .addPlaylist(DataConverter.convertPlaylistToPlaylistEntity(playlist))
        return getPlaylists()
    }

    override suspend fun removePlaylist(playlistId: Long) {
        appDatabase.getPlaylistDao().removePlaylist(playlistId)
    }

    override suspend fun getPlaylists(): List<Playlist> {
        return appDatabase.getPlaylistDao().getAllPlaylists()
            .map {
                val tracks = appDatabase.getPlaylistDao().getTracksInPlaylist(it.id)
                DataConverter.convertPlaylistEntityToPlaylist(it, tracks)
            }
    }

    override fun getTracksInPlaylist(playlistId: Long): Flow<Track> {
        return flow {
            appDatabase.getPlaylistDao().getTracksInPlaylist(playlistId)
                .forEach { emit(DataConverter.convertTrackEntityToTrack(it)) }
        }
        
    override suspend fun getPlaylist(playlistId: Long): Resource<Playlist> {
        val playlistEntity = appDatabase.getPlaylistDao().getPlaylistById(playlistId)
        return if (playlistEntity != null) {
            Resource.Success(DataConverter.convertPlaylistEntityToPlaylist(playlistEntity))
        } else {
            Resource.ClientError(null)
        }
    }

    override suspend fun addTrackToPlaylist(playlistId: Long, track: Track): Boolean {
        val trackEntity = DataConverter.convertTrackToTrackEntity(track)
        if (isTrackInPlaylist(playlistId, trackEntity.trackId)) {
            return false
        } else {
            addTrackToPlaylist(playlistId, trackEntity)
            return true
        }
    }

    override suspend fun addTrackToFavorites(track: Track) {
        val trackEntity = DataConverter.convertTrackToTrackEntity(track)
        addTrackToPlaylist(favoritesPlaylistId, trackEntity)
    }

    override suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long) {
        appDatabase.getPlaylistDao().removeTrackFromPlaylist(trackId, playlistId)
    }

    override suspend fun removeTrackFromFavorites(trackId: Long) {
        removeTrackFromPlaylist(favoritesPlaylistId, trackId)
    }

    private suspend fun addTrackToPlaylist(playlistId: Long, track: TrackEntity) {
        appDatabase.getTrackDao().addTrack(track)
        appDatabase.getPlaylistDao().addTrackToPlaylist(
            PlaylistTrackAssociationEntity(
                id = 0,
                trackId = track.trackId,
                playlistId = playlistId,
                createdAt = System.currentTimeMillis()
            )
        )
    }

}