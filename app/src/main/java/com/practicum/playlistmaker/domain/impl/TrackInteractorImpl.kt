package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.TrackInteractor
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.models.Track
import java.util.concurrent.Executors
import java.util.function.Consumer

class TrackInteractorImpl(private val trackRepository: TrackRepository) : TrackInteractor {

    override fun searchTracks(query: String, consumer: Consumer<List<Track>>) {
        Executors.newSingleThreadExecutor().execute {
            consumer.accept(trackRepository.searchTrack(query))
        }
    }

    override fun addTrackToHistory(track: Track) {
        Executors.newSingleThreadExecutor().execute {
            trackRepository.addTrackToHistory(track)
        }
    }

    override fun clearHistory() {
        Executors.newSingleThreadExecutor().execute {
            trackRepository.clearHistory()
        }
    }

    override fun getTrackInHistory(position: Int, consumer: Consumer<Track>) {
        Executors.newSingleThreadExecutor().execute {
            consumer.accept(trackRepository.getTrackInHistory(position))
        }
    }

    override fun getHistorySize(consumer: Consumer<Int>) {
        Executors.newSingleThreadExecutor().execute {
            consumer.accept(trackRepository.getHistorySize())
        }
    }

    override fun getAllHistory(consumer: Consumer<List<Track>>) {
        Executors.newSingleThreadExecutor().execute {
            consumer.accept(trackRepository.getAllHistory())
        }
    }
}