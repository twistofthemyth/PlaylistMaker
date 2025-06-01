package com.practicum.playlistmaker.util

import android.content.Context
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

    fun provideSearchRepo(context: Context): SearchRepository {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return ITunesSearchRepository(ITunesClient(connectivityManager))
    }

    fun provideSettingsRepo(sharedPref: SharedPreferences): SettingsRepository {
        return LocalSettingsRepository(sharedPref)
    }

    fun provideHistoryRepo(sharedPref: SharedPreferences): HistoryRepository {
        return LocalHistoryRepository(sharedPref)
    }

    fun provideSearchHistoryInteractor(sharedPref: SharedPreferences): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(provideHistoryRepo(sharedPref))
    }

    fun provideSearchInteractor(context: Context): SearchInteractor {
        return SearchInteractorImpl(provideSearchRepo(context))
    }

    fun provideSettingsInteractor(sharedPref: SharedPreferences): SettingsInteractor {
        return SettingsInteractorImpl(provideSettingsRepo(sharedPref))
    }

    fun provideTrackPlayer(track: Track, positionConsumer: Consumer<String>): TrackPlayer {
        return TrackPlayerImpl(track.previewUrl, positionConsumer)
    }
}