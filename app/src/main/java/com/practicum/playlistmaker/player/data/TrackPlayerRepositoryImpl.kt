package com.practicum.playlistmaker.player.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.player.domain.api.TrackPlayerRepository
import com.practicum.playlistmaker.search.domain.models.Track
import androidx.core.content.edit

class TrackPlayerRepositoryImpl(private val sharedPreferences: SharedPreferences,
                                private val gson: Gson) : TrackPlayerRepository {

    override fun getTrackForPlaying(): Track {
        return gson.fromJson(sharedPreferences.getString(TRACK_FOR_PLAYING_PREF_KEY, null), Track::class.java)
    }

    override fun setTrackForPlaying(track: Track) {
        sharedPreferences.edit(commit = true) {
            putString(
                TRACK_FOR_PLAYING_PREF_KEY,
                gson.toJson(track)
            )
        }
    }


    companion object {
        const val TRACK_FOR_PLAYING_PREF_KEY = "track_for_playing"
    }
}