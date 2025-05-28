package com.practicum.playlistmaker.data.dto

data class SearchSongRequest(
    val term: String,
    val entity: String
)