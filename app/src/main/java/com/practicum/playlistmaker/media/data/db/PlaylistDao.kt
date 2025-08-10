package com.practicum.playlistmaker.media.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface PlaylistDao {
    enum class SystemPlaylist(val displayName: String) {
        FAVORITES("Favorites")
    }

    @Insert(entity = PlaylistEntity::class, onConflict = REPLACE)
    suspend fun addPlaylist(playlist: PlaylistEntity)

    @Update(entity = PlaylistEntity::class, onConflict = REPLACE)
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Transaction
    suspend fun removePlaylist(playlistId: Long) {
        removeAllTracksFromPlaylist(playlistId)
        deletePlaylist(playlistId)
    }

    @Query("DELETE FROM playlists_table WHERE id = :playlistId")
    suspend fun deletePlaylist(playlistId: Long)

    @Query("SELECT * FROM playlists_table WHERE is_system = 0")
    suspend fun getAllPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlists_table WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: Long): PlaylistEntity?

    @Transaction
    suspend fun getSystemPlaylist(type: SystemPlaylist): PlaylistEntity {
        val existing = getSystemPlaylistByName(type.displayName)

        if (existing != null) return existing

        val newPlaylist = PlaylistEntity(
            id = 0,
            name = type.displayName,
            description = "System ${type.displayName} playlist",
            image = "",
            isSystem = true
        )

        val id = addPlaylistAndGetId(newPlaylist)
        return newPlaylist.copy(id = id)
    }

    @Query("SELECT * FROM playlists_table WHERE is_system = 1 AND name = :name LIMIT 1")
    suspend fun getSystemPlaylistByName(name: String): PlaylistEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylistAndGetId(playlist: PlaylistEntity): Long

    @Insert(onConflict = REPLACE)
    suspend fun addTrackToPlaylist(association: PlaylistTrackAssociationEntity)

    @Query("DELETE FROM playlist_association_table WHERE playlist_id = :playlistId")
    suspend fun removeAllTracksFromPlaylist(playlistId: Long)

    @Query("DELETE FROM playlist_association_table WHERE track_id = :trackId AND playlist_id = :playlistId")
    suspend fun removeTrackFromPlaylist(trackId: Long, playlistId: Long)

    @Transaction
    @Query(
        """
        SELECT * FROM tracks_table
        INNER JOIN playlist_association_table 
        ON tracks_table.track_id = playlist_association_table.track_id
        WHERE playlist_association_table.playlist_id = :playlistId
        ORDER BY playlist_association_table.created_at DESC
    """
    )
    suspend fun getTracksInPlaylist(playlistId: Long): List<TrackEntity>

    @Query(
        """
        SELECT COUNT(*) FROM playlist_association_table 
        WHERE track_id = :trackId AND playlist_id = :playlistId
    """
    )
    suspend fun isTrackInPlaylist(trackId: Long, playlistId: Long): Int
}