package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.SearchHistory
import com.practicum.playlistmaker.search.domain.models.Track

interface SearchHistoryInteractor {
    fun getSearchHistory(): SearchHistory
    fun addTrackToHistory(track: Track)
    fun clearSearchHistory()
    fun updateSearchQuery(query: String)
}