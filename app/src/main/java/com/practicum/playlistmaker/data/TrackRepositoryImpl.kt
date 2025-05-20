package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.HistoryTrackDto
import com.practicum.playlistmaker.data.dto.ITunesTrackDto
import com.practicum.playlistmaker.data.dto.SearchSongRequest
import com.practicum.playlistmaker.data.dto.SearchSongResponse
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackRepositoryImpl(
    private val iTunesClient: ITunesClient,
    private val sharedPrefClient: SharedPrefClient
) : TrackRepository {

    override fun searchTrack(query: String): List<Track> {
        val request = SearchSongRequest(query, "song")
        val result = iTunesClient.doRequest(request)
        if (result.statusCode == 200) {
            return (result as SearchSongResponse).results.map {
                convertNetworkTrackToTrack(it)
            }
        } else {
            return emptyList()
        }
    }

    override fun addTrackToHistory(track: Track) {
        sharedPrefClient.addTrackToHistory(convertTrackToHistoryTrack(track))
    }

    override fun clearHistory() {
        sharedPrefClient.clearHistory()
    }

    override fun getTrackInHistory(position: Int): Track {
        return convertHistoryTrackToTrack(sharedPrefClient.getTrackInHistory(position))
    }

    override fun getHistorySize(): Int {
        return sharedPrefClient.getHistorySize()
    }

    override fun getAllHistory(): List<Track> {
        TODO("Not yet implemented")
    }

    private fun convertNetworkTrackToTrack(iTunesTrackDto: ITunesTrackDto): Track {
        return Track(
            iTunesTrackDto.trackName,
            iTunesTrackDto.artistName,
            if (iTunesTrackDto.trackTimeMillis == 0L) "0:00" else TRACK_TIME_FORMATTER.format(
                iTunesTrackDto.trackTimeMillis
            ),
            iTunesTrackDto.artworkUrl100,
            iTunesTrackDto.artworkUrl100.replaceAfterLast("/", "512x512bb.jpg"),
            iTunesTrackDto.collectionName,
            iTunesTrackDto.releaseDate,
            iTunesTrackDto.primaryGenreName,
            iTunesTrackDto.country,
            iTunesTrackDto.previewUrl
        )
    }

    private fun convertHistoryTrackToTrack(historyTrackDto: HistoryTrackDto): Track {
        return Track(
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

    private fun convertTrackToHistoryTrack(track: Track): HistoryTrackDto {
        return HistoryTrackDto(
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

    companion object {
        private val TRACK_TIME_FORMATTER = SimpleDateFormat("mm:ss", Locale.getDefault())
    }
}