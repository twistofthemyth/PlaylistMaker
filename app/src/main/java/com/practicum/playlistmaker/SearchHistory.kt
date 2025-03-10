package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.model.Track
import java.lang.reflect.Type

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    private var history = loadHistory()

    fun addTrackToHistory(track: Track) {
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

    fun getTrackInHistory(position: Int): Track {
        return history[position]
    }

    fun getHistorySize(): Int {
        return history.size
    }

    private fun loadHistory(): MutableList<Track> {
        val json = sharedPreferences.getString(HISTORY_PREF_KEY, null)
        return if (!json.isNullOrEmpty()) {
            val type: Type = object : TypeToken<MutableList<Track>>() {}.type
            Gson().fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    private fun saveHistory() {
        val json = Gson().toJson(history)
        sharedPreferences.edit()
            .putString(HISTORY_PREF_KEY, json)
            .apply()
    }

    companion object {
        const val HISTORY_PREF_KEY = "track_history"
    }
}