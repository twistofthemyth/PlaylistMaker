package com.practicum.playlistmaker.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.dto.HistoryTrackDto
import com.practicum.playlistmaker.domain.models.Track
import java.lang.reflect.Type

class SharedPrefClient(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {

    private var history = loadHistory()

    fun addTrackToHistory(track: HistoryTrackDto) {
        if (history.contains(track)) {
            history.remove(track)
        }
        if (history.size >= 10) {
            history = history.subList(0, 9)
        }

        val reversedHistory = history.asReversed()
        reversedHistory.add(track)
        saveHistory()
    }

    fun clearHistory() {
        history.clear()
        saveHistory()
    }

    fun getTrackInHistory(position: Int): HistoryTrackDto {
        return history[position]
    }

    fun getHistorySize(): Int {
        return history.size
    }

    private fun loadHistory(): MutableList<HistoryTrackDto> {
        val json = sharedPreferences.getString(HISTORY_PREF_KEY, null)
        return if (!json.isNullOrEmpty()) {
            val type: Type = object : TypeToken<MutableList<Track>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    private fun saveHistory() {
        val json = gson.toJson(history)
        sharedPreferences.edit {
            putString(HISTORY_PREF_KEY, json)
        }
    }

    companion object {
        const val HISTORY_PREF_KEY = "track_history"
    }
}