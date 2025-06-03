package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.domain_utils.Resource

interface SearchRepository {
    fun searchTracks(query: String): Resource<List<Track>>
}