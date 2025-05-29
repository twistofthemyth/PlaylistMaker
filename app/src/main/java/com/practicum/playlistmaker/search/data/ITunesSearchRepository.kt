package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.dto.SearchSongRequest
import com.practicum.playlistmaker.search.data.dto.SearchSongResponse
import com.practicum.playlistmaker.search.data.network.ITunesClient
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.domain_utils.Resource

class ITunesSearchRepository(
    private val iTunesClient: ITunesClient,
) : SearchRepository {

    override fun searchTracks(query: String): Resource<List<Track>> {
        val request = SearchSongRequest(query, "song")
        val result = iTunesClient.doRequest(request)
        return when (result.statusCode) {
            -1 -> Resource.ClientError()
            200 -> {
                var data = (result as SearchSongResponse).results.map {
                    SearchDataConverter.convertNetworkTrackToTrack(it)
                }
                Resource.Success(data)
            }

            else -> Resource.ServerError()
        }
    }
}