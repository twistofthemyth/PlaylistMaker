package com.practicum.playlistmaker.di
import android.content.Context
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

    single<SearchRepository> {
        ITunesSearchRepository(get())
    }

    single<SettingsRepository> {
        LocalSettingsRepository(get())
    }

    single<HistoryRepository> {
        LocalHistoryRepository(get())
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

    single {
        val context = get<Context>()
        context.getSharedPreferences("PM_REPOSITORY_MODULE", Context.MODE_PRIVATE)
    }
}