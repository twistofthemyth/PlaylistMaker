package com.practicum.playlistmaker.media.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS favorite_tracks_table")
        db.execSQL("DROP TABLE IF EXISTS tracks_table")
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `tracks_table` (
                `track_id` INTEGER NOT NULL PRIMARY KEY,
                `track_name` TEXT NOT NULL,
                `artist_name` TEXT NOT NULL,
                `track_time` TEXT NOT NULL,
                `artwork_url` TEXT NOT NULL,
                `cover_url` TEXT NOT NULL,
                `collection_name` TEXT NOT NULL,
                `release_date` TEXT NOT NULL,
                `primary_genre_name` TEXT NOT NULL,
                `country_name` TEXT NOT NULL,
                `track_url` TEXT NOT NULL
            )
        """
        )
    }
}