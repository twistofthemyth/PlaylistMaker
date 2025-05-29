package com.practicum.playlistmaker.search.ui.view_model

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.Creator

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val searchInteractor = Creator.provideSearchInteractor(getApplication())
    private val searchHistoryInteractor = Creator.provideSearchHistoryInteractor(
        application.getSharedPreferences(
            "SearchViewModel",
            MODE_PRIVATE
        )
    )

    private var state = MutableLiveData<SearchViewState>(SearchViewState.Loading())
    fun getState(): LiveData<SearchViewState> = state


    fun search(query: String) {
        searchInteractor.searchTracks(query)
    }


    sealed interface SearchViewState {
        class ShowHistory(tracks: List<Track>) : SearchViewState

        class Loading() : SearchViewState

        class NetworkError() : SearchViewState

        class ShowSearchResult(tracks: List<Track>) : SearchViewState
    }
}