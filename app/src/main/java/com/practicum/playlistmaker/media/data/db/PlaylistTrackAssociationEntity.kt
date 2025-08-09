package com.practicum.playlistmaker.media.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("playlist_association_table")
data class PlaylistTrackAssociationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "track_id")
    val trackId: Long,
    @ColumnInfo(name = "playlist_id")
    val playlistId: Long,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)