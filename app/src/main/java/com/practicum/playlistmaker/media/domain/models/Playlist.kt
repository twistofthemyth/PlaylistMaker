package com.practicum.playlistmaker.media.domain.models

import com.practicum.playlistmaker.search.domain.models.Track

data class Playlist(
    val name: String,
    val description: String,
    val image: String,
    val track: List<Track>
)