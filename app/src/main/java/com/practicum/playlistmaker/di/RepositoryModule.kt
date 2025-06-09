package com.practicum.playlistmaker.di

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
import org.koin.dsl.module

val repositoryModule = module {

    factory<SearchRepository> {
        ITunesSearchRepository(get())
    }

    factory<SettingsRepository> {
        LocalSettingsRepository(get(), get())
    }

    factory<HistoryRepository> {
        LocalHistoryRepository(get(), get())
    }

    factory<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    factory<SearchInteractor> {
        SearchInteractorImpl(get())
    }

    factory<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }
}