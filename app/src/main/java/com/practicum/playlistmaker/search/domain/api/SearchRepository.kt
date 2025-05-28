package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.SearchHistory
import com.practicum.playlistmaker.search.domain.models.Track

interface SearchRepository {
    fun searchTracks(query: String): List<Track>
}