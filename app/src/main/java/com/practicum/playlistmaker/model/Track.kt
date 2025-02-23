package com.practicum.playlistmaker.model

import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String
) {
    constructor(
        trackName: String,
        artistName: String,
        trackTime: Long,
        artworkUrl100: String
    ) : this(trackName, artistName, TRACK_TIME_FORMATTER.format(trackTime), artworkUrl100)

    companion object {
        private val TRACK_TIME_FORMATTER = SimpleDateFormat("mm:ss", Locale.getDefault()).format(293000L)
    }
}

