package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.domain_utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.Executors

class SearchInteractorImpl(private val repo: SearchRepository) : SearchInteractor {
    override fun searchTracks(query: String): Flow<Resource<List<Track>>>  =  repo.searchTracks(query)
}