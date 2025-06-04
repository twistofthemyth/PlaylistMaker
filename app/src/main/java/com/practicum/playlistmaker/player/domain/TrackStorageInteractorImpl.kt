package com.practicum.playlistmaker.player.domain

import com.practicum.playlistmaker.player.domain.api.TrackPlayerRepository
import com.practicum.playlistmaker.player.domain.api.TrackStorageInteractor
import com.practicum.playlistmaker.search.domain.models.Track

class TrackStorageInteractorImpl(private val trackPlayerRepository: TrackPlayerRepository) : TrackStorageInteractor {

    override fun getTrackForPlaying() : Track {
        return trackPlayerRepository.getTrackForPlaying()
    }

    override fun setTrackForPlaying(track: Track) {
        return trackPlayerRepository.setTrackForPlaying(track)
    }
}