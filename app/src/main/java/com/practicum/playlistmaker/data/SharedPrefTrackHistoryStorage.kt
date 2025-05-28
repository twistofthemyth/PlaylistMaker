package com.practicum.playlistmaker.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.dto.HistoryTrackDto
import com.practicum.playlistmaker.domain.models.Track
import java.lang.reflect.Type

class SharedPrefTrackHistoryStorage(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {

    private var history = loadTrackHistory()

    fun addTrackToHistory(track: HistoryTrackDto) {
        if (history.contains(track)) {
            history.remove(track)
        }
        if (history.size >= 10) {
            history = history.subList(0, 9)
        }

        val reversedHistory = history.asReversed()
        reversedHistory.add(track)
        saveTrackHistory()
    }

    fun clearTrackHistory() {
        history.clear()
        saveTrackHistory()
    }

    fun getTrackInHistory(position: Int): HistoryTrackDto {
        return history[position]
    }

    fun getTrackHistorySize(): Int {
        return history.size
    }

    fun getTrackHistory(): List<HistoryTrackDto> {
        return history
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

    private fun saveTrackHistory() {
        val json = gson.toJson(history)
        sharedPreferences.edit {
            putString(HISTORY_PREF_KEY, json)
        }
    }

    companion object {
        const val HISTORY_PREF_KEY = "track_history"
    }
}