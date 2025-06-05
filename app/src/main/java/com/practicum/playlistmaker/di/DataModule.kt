package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.google.gson.Gson
import com.practicum.playlistmaker.player.data.TrackPlayerImpl
import com.practicum.playlistmaker.player.domain.api.TrackPlayer
import com.practicum.playlistmaker.search.data.network.ITunesClient
import org.koin.dsl.module

val dataModule = module {

    factory {
        ITunesClient(get())
    }

    factory<TrackPlayer> {
        TrackPlayerImpl(get())
    }

    factory<MediaPlayer> {
        MediaPlayer()
    }

    factory<Gson> {
        Gson()
    }

    single {
        val context = get<Context>()
        context.getSharedPreferences("PM_REPOSITORY_MODULE", Context.MODE_PRIVATE)
    }
}