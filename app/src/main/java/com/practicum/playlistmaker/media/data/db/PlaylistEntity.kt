package com.practicum.playlistmaker.media.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("playlists_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val description: String,
    val image: String,
    @ColumnInfo(name = "is_system")
    val isSystem: Boolean = false
)