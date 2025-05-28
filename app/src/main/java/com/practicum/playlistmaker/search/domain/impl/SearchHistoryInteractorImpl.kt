package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.models.SearchHistory
import com.practicum.playlistmaker.search.domain.models.Track

class SearchHistoryInteractorImpl(private val repo: HistoryRepository) :
    SearchHistoryInteractor {

    override fun getSearchHistory(): SearchHistory {
        return repo.getSearchHistory()
    }

    override fun addTrackToHistory(track: Track) {
        repo.getSearchHistory().records.add(track)
    }

    override fun clearSearchHistory() {
        repo.getSearchHistory().apply {
            records.clear()
            query = ""
        }
    }

    override fun updateSearchQuery(query: String) {
        repo.getSearchHistory().query = query
    }
}