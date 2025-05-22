package com.practicum.playlistmaker.data.dto

data class SearchSongResponse(val resultCount: Int, val results: List<ITunesTrackDto>) : Response()