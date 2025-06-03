package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.data.TrackPlayerImpl
import com.practicum.playlistmaker.player.domain.TrackPlayer
import com.practicum.playlistmaker.search.data.network.ITunesClient
import com.practicum.playlistmaker.search.domain.models.Track
import org.koin.dsl.module

val dataModule = module {

    factory {
        ITunesClient(get())
    }

    factory<TrackPlayer> {
        (track: Track) -> TrackPlayerImpl(track.previewUrl)
    }
}