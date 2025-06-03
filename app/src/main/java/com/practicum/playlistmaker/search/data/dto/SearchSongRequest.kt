package com.practicum.playlistmaker.search.data.dto

data class SearchSongRequest(
    val term: String,
    val entity: String
)