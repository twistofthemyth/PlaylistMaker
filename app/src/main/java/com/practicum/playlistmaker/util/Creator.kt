package com.practicum.playlistmaker.util

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.practicum.playlistmaker.player.data.TrackPlayerImpl
import com.practicum.playlistmaker.player.domain.TrackPlayer
import com.practicum.playlistmaker.search.data.ITunesSearchRepository
import com.practicum.playlistmaker.search.data.LocalHistoryRepository
import com.practicum.playlistmaker.search.data.network.ITunesClient
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.settings.data.LocalSettingsRepository
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import java.util.function.Consumer

object Creator {

    private lateinit var app: Application

    fun init(app: Application) {
        this.app = app
    }

    fun provideSearchRepo(): SearchRepository {
        val connectivityManager = app.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return ITunesSearchRepository(ITunesClient(connectivityManager))
    }

    fun provideSettingsRepo(): SettingsRepository {
        return LocalSettingsRepository(provideSharedPref())
    }

    fun provideHistoryRepo(): HistoryRepository {
        return LocalHistoryRepository(provideSharedPref())
    }

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(provideHistoryRepo())
    }

    fun provideSearchInteractor(): SearchInteractor {
        return SearchInteractorImpl(provideSearchRepo())
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(provideSettingsRepo())
    }

    fun provideTrackPlayer(track: Track, positionConsumer: Consumer<String>): TrackPlayer {
        return TrackPlayerImpl(track.previewUrl, positionConsumer)
    }

    private fun provideSharedPref() : SharedPreferences {
        return app.getSharedPreferences("Creator", MODE_PRIVATE)
    }
}