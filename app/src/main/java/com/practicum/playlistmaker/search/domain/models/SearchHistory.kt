package com.practicum.playlistmaker.search.domain.models

data class SearchHistory(var query: String, val records: MutableList<Track>)