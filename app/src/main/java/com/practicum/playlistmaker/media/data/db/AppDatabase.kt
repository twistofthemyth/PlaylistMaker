package com.practicum.playlistmaker.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 3,
    entities = [TrackEntity::class, PlaylistEntity::class, PlaylistTrackAssociationEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getPlaylistDao(): PlaylistDao

    abstract fun getTrackDao(): TrackDao
}