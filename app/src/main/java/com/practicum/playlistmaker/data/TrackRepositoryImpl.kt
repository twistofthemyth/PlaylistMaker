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
    private val sharedPrefTrackHistoryStorage: SharedPrefTrackHistoryStorage,
    private val sharedPrefInputStorage: SharedPrefInputStorage
) : TrackRepository {

    override fun searchTrack(query: String, onFail: Runnable): Pair<Boolean, List<Track>> {
        val request = SearchSongRequest(query, "song")
        val result = iTunesClient.doRequest(request)
        if (result.statusCode == 200) {
            return Pair(
                true,
                (result as SearchSongResponse).results.map { convertNetworkTrackToTrack(it) }
            )
        } else {
            onFail.run()
            return Pair(
                false,
                emptyList()
            )
        }
    }

    override fun addTrackToHistory(track: Track) {
        sharedPrefTrackHistoryStorage.addTrackToHistory(convertTrackToHistoryTrack(track))
    }

    override fun clearTrackHistory() {
        sharedPrefTrackHistoryStorage.clearTrackHistory()
    }

    override fun getSearchHistory(): List<Track> {
        return sharedPrefTrackHistoryStorage.getTrackHistory()
            .map { convertHistoryTrackToTrack(it) }
    }

    override fun saveSearchInput(value: String) {
        sharedPrefInputStorage.saveStringValue("search_input", value)
    }


    override fun loadSavedSearchInput(): String {
        return sharedPrefInputStorage.loadStringValue("search_input")
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
            iTunesTrackDto.releaseDate ?: "",
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