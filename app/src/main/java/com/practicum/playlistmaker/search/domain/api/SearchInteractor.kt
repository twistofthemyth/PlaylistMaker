package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.domain_utils.Resource
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun searchTracks(query: String) : Flow<Resource<List<Track>>>
}