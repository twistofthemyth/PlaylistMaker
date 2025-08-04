package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmaker.media.data.db.AppDatabase
import com.practicum.playlistmaker.media.data.db.MIGRATION_1_2
import com.practicum.playlistmaker.player.data.TrackPlayerImpl
import com.practicum.playlistmaker.player.domain.api.TrackPlayer
import com.practicum.playlistmaker.search.data.network.ITunesClient
import org.koin.android.ext.koin.androidContext
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

    single<AppDatabase> {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .addMigrations(MIGRATION_1_2)
            .build()
    }
}