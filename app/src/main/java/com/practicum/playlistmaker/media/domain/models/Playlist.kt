package com.practicum.playlistmaker.media.domain.models

import androidx.compose.runtime.Immutable
import com.practicum.playlistmaker.search.domain.models.Track

@Immutable
data class Playlist(
    val id: Long,
    val name: String,
    val description: String,
    val image: String?,
    val track: List<Track>
)