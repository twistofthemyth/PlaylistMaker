package com.practicum.playlistmaker.search.data.dto

class ITunesTrackDto(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    var trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String?,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
)