package com.practicum.playlistmaker.media.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tracks_table")
data class TrackEntity(
    @PrimaryKey
    @ColumnInfo(name = "track_id")
    val trackId: Long,
    @ColumnInfo(name = "track_name")
    val trackName: String,
    @ColumnInfo(name = "artist_name")
    val artistName: String,
    @ColumnInfo(name = "track_time")
    val trackTime: String,
    @ColumnInfo(name = "artwork_url")
    val artwork: String,
    @ColumnInfo(name = "cover_url")
    val coverArtwork: String,
    @ColumnInfo(name = "collection_name")
    val collectionName: String,
    @ColumnInfo(name = "release_date")
    val releaseDate: String,
    @ColumnInfo(name = "primary_genre_name")
    val primaryGenreName: String,
    @ColumnInfo(name = "country_name")
    val country: String,
    @ColumnInfo(name = "track_url")
    val previewUrl: String
)