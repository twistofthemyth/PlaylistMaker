package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.dto.SearchSongRequest
import com.practicum.playlistmaker.search.data.dto.SearchSongResponse
import com.practicum.playlistmaker.search.data.network.ITunesClient
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.models.Track

class ITunesSearchRepository(
    private val iTunesClient: ITunesClient,
) : SearchRepository {

    override fun searchTracks(query: String): List<Track> {
        val request = SearchSongRequest(query, "song")
        val result = iTunesClient.doRequest(request)
        return if (result.statusCode == 200) {
            (result as SearchSongResponse).results.map {
                SearchDataConverter.convertNetworkTrackToTrack(
                    it
                )
            }
        } else {
            emptyList()
        }
    }
}