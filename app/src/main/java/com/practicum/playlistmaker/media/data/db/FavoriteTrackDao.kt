package com.practicum.playlistmaker.media.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavoriteTrackDao {

    @Update(entity = TrackEntity::class, onConflict = REPLACE)
    suspend fun updateTrack(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun removeTrack(track: TrackEntity)

    @Insert(entity = TrackEntity::class, onConflict = REPLACE)
    suspend fun addTrack(track: TrackEntity)

    @Query("SELECT COUNT(*) FROM favorite_tracks_table WHERE track_id = :id")
    suspend fun countTrackById(id: Long): Int

    @Query("SELECT * FROM favorite_tracks_table ORDER BY created_at DESC")
    suspend fun getAllTracks(): List<TrackEntity>
}