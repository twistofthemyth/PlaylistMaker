package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.dto.SearchSongRequest
import com.practicum.playlistmaker.search.data.dto.SearchSongResponse
import com.practicum.playlistmaker.search.data.network.ITunesClient
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.domain_utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ITunesSearchRepository(
    private val iTunesClient: ITunesClient,
) : SearchRepository {

    override fun searchTracks(query: String): Flow<Resource<List<Track>>> = flow {
        val request = SearchSongRequest(query, "song")
        val result = iTunesClient.doRequest(request)
        when (result.statusCode) {
            -1 -> emit(Resource.ClientError())
            200 -> {
                val data = (result as SearchSongResponse).results.map {
                    SearchDataConverter.convertNetworkTrackToTrack(it)
                }
                emit(Resource.Success(data))
            }
            else -> emit(Resource.ServerError())
        }
    }
}