package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.createplaylist.ui.view_model.CreatePlaylistViewModel
import com.practicum.playlistmaker.media.ui.view_model.MediaViewModel
import com.practicum.playlistmaker.player.ui.view_model.TrackViewModel
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { (trackId: String) ->
        TrackViewModel(trackId, get(), get(), get())
    }

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get())
    }

    viewModel {
        MediaViewModel(get())
    }

    viewModel {
        CreatePlaylistViewModel()
    }
}