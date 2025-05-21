package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.data.ITunesClient
import com.practicum.playlistmaker.data.SharedPrefInputStorage
import com.practicum.playlistmaker.data.SharedPrefTrackHistoryStorage
import com.practicum.playlistmaker.data.TrackRepositoryImpl
import com.practicum.playlistmaker.domain.api.TrackInteractor
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.impl.TrackInteractorImpl

object Creator {

    fun provideTrackInteractor(sharedPreferences: SharedPreferences): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository(sharedPreferences))
    }

    private fun getTrackRepository(sharedPreferences: SharedPreferences): TrackRepository {
        return TrackRepositoryImpl(
            ITunesClient(), SharedPrefTrackHistoryStorage(sharedPreferences, Gson()),
            SharedPrefInputStorage(sharedPreferences)
        )
    }
}