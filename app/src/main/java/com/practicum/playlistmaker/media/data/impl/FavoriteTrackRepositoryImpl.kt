package com.practicum.playlistmaker.media.data.impl

import com.practicum.playlistmaker.media.data.db.AppDatabase
import com.practicum.playlistmaker.media.domain.api.FavoriteTracksRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.data_utils.DataConverter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTrackRepositoryImpl(val appDatabase: AppDatabase) : FavoriteTracksRepository {

    override suspend fun isTrackInFavorites(track: Track): Boolean {
        return appDatabase.getFavoriteTracksDao().countTrackById(track.trackId.toLong()) != 0
    }

    override suspend fun addTrackToFavorites(track: Track) {
        appDatabase.getFavoriteTracksDao()
            .addTrack(DataConverter.convertTrackToTrackEntity(track))
    }

    override suspend fun removeTrackFromFavorites(track: Track) {
        appDatabase.getFavoriteTracksDao()
            .removeTrack(DataConverter.convertTrackToTrackEntity(track))
    }

    override fun getFavoritesTrack(): Flow<Track> {
        return flow {
            appDatabase.getFavoriteTracksDao().getAllTracks()
                .forEach { emit(DataConverter.convertTrackEntityToTrack(it)) }
        }
    }
}