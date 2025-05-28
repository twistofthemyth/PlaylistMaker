package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.SearchHistory

interface HistoryRepository {
    fun getSearchHistory(): SearchHistory
    fun updateSearchHistory(history: SearchHistory)
    fun clearSearchHistory()
}