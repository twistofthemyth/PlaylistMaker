package com.practicum.playlistmaker.search.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.search.data.dto.HistoryTrackDto
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.models.SearchHistory
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.data_utils.DataConverter.convertHistoryTrackToTrack
import com.practicum.playlistmaker.util.data_utils.DataConverter.convertTrackToHistoryTrack
import java.lang.reflect.Type

class LocalHistoryRepository(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson) : HistoryRepository {

    override fun getSearchHistory(): SearchHistory {
        val query = loadLastQuery()
        val records = loadTrackHistory().map { convertHistoryTrackToTrack(it) }
        return SearchHistory(query, records as MutableList<Track>)
    }

    override fun updateSearchHistory(history: SearchHistory) {
        saveLastQuery(history.query)
        saveTrackHistory(history.records.map { convertTrackToHistoryTrack(it) })
    }

    override fun clearSearchHistory() {
        saveLastQuery("")
        saveTrackHistory(listOf())
    }

    private fun loadLastQuery(): String {
        return sharedPreferences.getString(HISTORY_QUERY_PREF_KEY, "").toString();
    }

    private fun saveLastQuery(query: String) {
        sharedPreferences.edit { putString(HISTORY_QUERY_PREF_KEY, query) }
    }

    private fun loadTrackHistory(): MutableList<HistoryTrackDto> {
        val json = sharedPreferences.getString(HISTORY_PREF_KEY, null)
        return if (!json.isNullOrEmpty()) {
            val type: Type = object : TypeToken<MutableList<HistoryTrackDto>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    private fun saveTrackHistory(history: List<HistoryTrackDto>) {
        val json = gson.toJson(history)
        sharedPreferences.edit {
            putString(HISTORY_PREF_KEY, json)
        }
    }

    companion object {
        const val HISTORY_PREF_KEY = "track_history"
        const val HISTORY_QUERY_PREF_KEY = "search_input"
    }
}