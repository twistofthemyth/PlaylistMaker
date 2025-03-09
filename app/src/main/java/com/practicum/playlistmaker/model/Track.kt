package com.practicum.playlistmaker.model

import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackName: String,
    val artistName: String,
    var trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String
) {

    fun getTrackTime() = if (trackTimeMillis == 0L) "0:00" else TRACK_TIME_FORMATTER.format(trackTimeMillis)

    fun getCoverArtwork() = artworkUrl100.replaceAfterLast("/", "512x512bb.jpg")

    companion object {
        private val TRACK_TIME_FORMATTER = SimpleDateFormat("mm:ss", Locale.getDefault())
    }
}

