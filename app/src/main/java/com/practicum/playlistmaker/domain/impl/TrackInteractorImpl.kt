package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.TrackInteractor
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.Runnable
import java.util.concurrent.Executors
import java.util.function.Consumer

class TrackInteractorImpl(private val trackRepository: TrackRepository) : TrackInteractor {

    override fun searchTracks(
        query: String,
        onSuccess: Consumer<List<Track>>,
        onFail: Runnable
    ) {
        Executors.newSingleThreadExecutor().execute {
            var searchResult = trackRepository.searchTrack(query, onFail)
            if (searchResult.first) {
                onSuccess.accept(searchResult.second)
            }
        }
    }

    override fun addTrackToHistory(track: Track) {
        trackRepository.addTrackToHistory(track)
    }

    override fun clearHistory() {
        trackRepository.clearTrackHistory()
    }

    override fun getSearchHistory(consumer: Consumer<List<Track>>) {
        Executors.newSingleThreadExecutor().execute {
            consumer.accept(trackRepository.getSearchHistory())
        }
    }

    override fun saveSearchQuery(value: String) {
        trackRepository.saveSearchInput(value)
    }

    override fun getSavedSearchQuery(): String {
        return trackRepository.loadSavedSearchInput()
    }
}