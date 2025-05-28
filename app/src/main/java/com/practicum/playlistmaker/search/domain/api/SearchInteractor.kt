package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface SearchInteractor {

    fun searchTracks(query: String): List<Track>
}