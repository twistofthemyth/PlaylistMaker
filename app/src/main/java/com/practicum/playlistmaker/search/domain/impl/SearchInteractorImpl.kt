package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.models.Track

class SearchInteractorImpl(private val repo: SearchRepository) : SearchInteractor {
    override fun searchTracks(query: String): List<Track> {
        return repo.searchTracks(query)
    }
}