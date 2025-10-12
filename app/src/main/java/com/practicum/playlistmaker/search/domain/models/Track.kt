package com.practicum.playlistmaker.search.domain.models

import androidx.compose.runtime.Immutable

@Immutable
data class Track(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artwork: String,
    val coverArtwork: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
)