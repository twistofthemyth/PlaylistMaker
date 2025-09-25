package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.dto.LookupEntityRequest
import com.practicum.playlistmaker.search.data.dto.SearchEntityRequest
import com.practicum.playlistmaker.search.data.dto.SearchEntityResponse
import com.practicum.playlistmaker.search.data.network.ITunesClient
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.data_utils.DataConverter
import com.practicum.playlistmaker.util.data_utils.Response
import com.practicum.playlistmaker.util.domain_utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ITunesSearchRepository(
    private val iTunesClient: ITunesClient,
) : SearchRepository {

    override fun searchTracks(query: String): Flow<Resource<List<Track>>> = flow {
        val request = SearchEntityRequest(query, "song")
        val result = iTunesClient.doRequest(request)
        emit(handleResult(result) {
            (result as SearchEntityResponse).results.map {
                DataConverter.convertNetworkTrackToTrack(it)
            }
        })
    }

    override suspend fun searchTrackById(id: String): Resource<Track> {
        val request = LookupEntityRequest(id)
        val result = iTunesClient.doRequest(request)

        return handleResult(result) {
            DataConverter.convertNetworkTrackToTrack((result as SearchEntityResponse).results.first())
        }
    }

    private fun <T> handleResult(result: Response, dataConverter: () -> T): Resource<T> {
        return when (result.statusCode) {
            -1 -> Resource.ClientError()
            200 -> Resource.Success(dataConverter.invoke())
            else -> Resource.ServerError()
        }
    }
}