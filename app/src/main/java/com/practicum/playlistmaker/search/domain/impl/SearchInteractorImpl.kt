package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.domain_utils.Resource

class SearchInteractorImpl(private val repo: SearchRepository) : SearchInteractor {
    override fun searchTracks(query: String, consumer: (Resource<List<Track>>) -> Unit) {
        consumer.invoke(repo.searchTracks(query))
    }
}