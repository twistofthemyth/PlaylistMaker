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
        var history = repo.getSearchHistory()
        history.records.remove(track)
        history.records.add(track)
        repo.updateSearchHistory(history)
    }

    override fun clearSearchHistory() {
        var history = repo.getSearchHistory()
        history.records.clear()
        history.query = ""
        repo.updateSearchHistory(history)
    }

    override fun updateSearchQuery(query: String) {
        var history = repo.getSearchHistory()
        history.query = query
        repo.updateSearchHistory(history)
    }
}