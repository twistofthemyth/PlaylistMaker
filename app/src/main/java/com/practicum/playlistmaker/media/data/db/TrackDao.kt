package com.practicum.playlistmaker.media.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query

@Dao
interface TrackDao {

    @Insert(onConflict = IGNORE)
    suspend fun addTrack(track: TrackEntity)

    @Delete
    suspend fun removeTrack(track: TrackEntity)

    @Query("SELECT * FROM tracks_table")
    suspend fun getAllTracks(): List<TrackEntity>

    @Query("SELECT * FROM tracks_table WHERE track_id = :trackId")
    suspend fun getTrackById(trackId: Long): TrackEntity?
}