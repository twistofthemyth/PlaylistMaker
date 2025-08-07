package com.practicum.playlistmaker.media.data.impl

import com.practicum.playlistmaker.media.data.db.AppDatabase
import com.practicum.playlistmaker.media.data.db.PlaylistDao
import com.practicum.playlistmaker.media.data.db.PlaylistTrackAssociationEntity
import com.practicum.playlistmaker.media.data.db.TrackEntity
import com.practicum.playlistmaker.media.domain.api.PlaylistRepository
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.data_utils.DataConverter
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

    override suspend fun isTrackInFavorites(track: Track): Boolean {
        return appDatabase.getPlaylistDao().isTrackInPlaylist(
            DataConverter.convertTrackToTrackEntity(track).trackId,
            favoritesPlaylistId
        ) > 0
    }

    override suspend fun isTrackInPlaylist(playlist: Playlist, track: Track): Boolean {
        return appDatabase.getPlaylistDao().isTrackInPlaylist(
            DataConverter.convertTrackToTrackEntity(track).trackId,
            playlist.id
        ) > 0
    }

    override fun getFavoritesTrack(): Flow<Track> {
        return getTracksInPlaylist(favoritesPlaylistId)
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.getPlaylistDao()
            .addPlaylist(DataConverter.convertPlaylistToPlaylistEntity(playlist))
    }

    override suspend fun removePlaylist(playlist: Playlist) {
        appDatabase.getPlaylistDao().removePlaylist(playlist.id)
    }

    override fun getPlaylists(): Flow<Playlist> {
        return flow {
            appDatabase.getPlaylistDao().getAllPlaylists()
                .forEach {
                    val tracks = appDatabase.getPlaylistDao().getTracksInPlaylist(it.id)
                    emit(
                        DataConverter.convertPlaylistEntityToPlaylist(it, tracks)
                    )
                }
        }
    }

    override fun getTracksInPlaylist(playlist: Playlist): Flow<Track> {
        return getTracksInPlaylist(DataConverter.convertPlaylistToPlaylistEntity(playlist).id)
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        val trackEntity = DataConverter.convertTrackToTrackEntity(track)
        addTrackToPlaylist(playlist.id, trackEntity)
    }

    override suspend fun addTrackToFavorites(track: Track) {
        val trackEntity = DataConverter.convertTrackToTrackEntity(track)
        addTrackToPlaylist(favoritesPlaylistId, trackEntity)
    }

    override suspend fun removeTrackFromPlaylist(playlist: Playlist, track: Track) {
        val playlistEntity = DataConverter.convertPlaylistToPlaylistEntity(playlist)
        val trackEntity = DataConverter.convertTrackToTrackEntity(track)
        removeTrackFromPlaylist(playlistEntity.id, trackEntity)
    }

    override suspend fun removeTrackFromFavorites(track: Track) {
        val trackEntity = DataConverter.convertTrackToTrackEntity(track)
        removeTrackFromPlaylist(favoritesPlaylistId, trackEntity)
    }

    private fun getTracksInPlaylist(playlistId: Long): Flow<Track> {
        return flow {
            appDatabase.getPlaylistDao().getTracksInPlaylist(playlistId)
                .forEach { emit(DataConverter.convertTrackEntityToTrack(it)) }
        }
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

    private suspend fun removeTrackFromPlaylist(playlistId: Long, track: TrackEntity) {
        appDatabase.getPlaylistDao().removeTrackFromPlaylist(track.trackId, playlistId)
    }

}