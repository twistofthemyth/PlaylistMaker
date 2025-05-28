package com.practicum.playlistmaker.util

import android.content.SharedPreferences
import com.practicum.playlistmaker.search.data.network.ITunesClient
import com.practicum.playlistmaker.search.data.ITunesSearchRepository
import com.practicum.playlistmaker.search.data.LocalHistoryRepository
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.practicum.playlistmaker.settings.data.LocalSettingsRepository
import com.practicum.playlistmaker.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.api.SettingsRepository
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl

object Creator {

    fun provideSearchRepo(): SearchRepository {
        return ITunesSearchRepository(ITunesClient())
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

    fun provideSearchInteractor(): SearchInteractor {
        return SearchInteractorImpl(provideSearchRepo())
    }

    fun provideSettingsInteractor(sharedPref: SharedPreferences): SettingsInteractor {
        return SettingsInteractorImpl(provideSettingsRepo(sharedPref))
    }
}