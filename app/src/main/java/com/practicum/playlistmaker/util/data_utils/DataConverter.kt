package com.practicum.playlistmaker.util.data_utils

import com.practicum.playlistmaker.media.data.db.TrackEntity
import com.practicum.playlistmaker.search.data.dto.HistoryTrackDto
import com.practicum.playlistmaker.search.data.dto.ITunesTrackDto
import com.practicum.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

object DataConverter {
    private val TRACK_TIME_FORMATTER = SimpleDateFormat("mm:ss", Locale.ENGLISH)

    fun convertNetworkTrackToTrack(iTunesTrackDto: ITunesTrackDto): Track {
        return Track(
            iTunesTrackDto.trackId,
            iTunesTrackDto.trackName,
            iTunesTrackDto.artistName,
            if (iTunesTrackDto.trackTimeMillis == 0L) "0:00" else TRACK_TIME_FORMATTER.format(
                iTunesTrackDto.trackTimeMillis
            ),
            iTunesTrackDto.artworkUrl100,
            iTunesTrackDto.artworkUrl100.replaceAfterLast("/", "512x512bb.jpg"),
            iTunesTrackDto.collectionName,
            iTunesTrackDto.releaseDate ?: "",
            iTunesTrackDto.primaryGenreName,
            iTunesTrackDto.country,
            iTunesTrackDto.previewUrl
        )
    }

    fun convertHistoryTrackToTrack(historyTrackDto: HistoryTrackDto): Track {
        return Track(
            historyTrackDto.trackId ?: "0",
            historyTrackDto.trackName,
            historyTrackDto.artistName,
            historyTrackDto.trackTime,
            historyTrackDto.artwork,
            historyTrackDto.coverArtwork.replaceAfterLast("/", "512x512bb.jpg"),
            historyTrackDto.collectionName,
            historyTrackDto.releaseDate,
            historyTrackDto.primaryGenreName,
            historyTrackDto.country,
            historyTrackDto.previewUrl
        )
    }

    fun convertTrackToHistoryTrack(track: Track): HistoryTrackDto {
        return HistoryTrackDto(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artwork,
            track.coverArtwork.replaceAfterLast("/", "512x512bb.jpg"),
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    fun convertTrackEntityToTrack(track: TrackEntity): Track {
        return Track(
            "${track.trackId}",
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artwork,
            track.coverArtwork,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    fun convertTrackToTrackEntity(track: Track): TrackEntity {
        return TrackEntity(
            track.trackId.toLong(),
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artwork,
            track.coverArtwork,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }
}