package com.practicum.playlistmaker.search.data.dto

import com.practicum.playlistmaker.util.data_utils.Response

data class SearchEntityResponse(val resultCount: Int, val results: List<ITunesTrackDto>) : Response()