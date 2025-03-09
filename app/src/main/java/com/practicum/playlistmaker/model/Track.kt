package com.practicum.playlistmaker.model

import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String
) {
    constructor(
        trackName: String,
        artistName: String,
        trackTime: Long,
        artworkUrl100: String,
        collectionName: String,
        releaseDate: String,
        primaryGenreName: String,
        country: String
    ) : this(
        trackName,
        artistName,
        TRACK_TIME_FORMATTER.format(trackTime),
        artworkUrl100,
        collectionName,
        releaseDate,
        primaryGenreName,
        country
    )

    companion object {
        private val TRACK_TIME_FORMATTER =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(293000L)
    }
}

