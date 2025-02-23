package com.practicum.playlistmaker.model

data class SearchSongResponse(val resultCount: Int, val results: List<Track>) {
}