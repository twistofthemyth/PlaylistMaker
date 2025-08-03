package com.practicum.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.domain_utils.Resource
import com.practicum.playlistmaker.util.ui_utils.TrackNavigatableViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    val searchInteractor: SearchInteractor,
    val searchHistoryInteractor: SearchHistoryInteractor
) : TrackNavigatableViewModel() {

    private val screenState = MutableLiveData(setUpDefaultState())
    private var searchJob: Job? = null
    private var latestSearchQuery: String? = null

    fun getScreenState(): LiveData<SearchViewState> = screenState

    fun repeatSearch() {
        if (latestSearchQuery != null) {
            search(latestSearchQuery.toString())
        }
    }

    fun cleanSearchQuery() {
        val history = searchHistoryInteractor.getSearchHistory()
        screenState.postValue(SearchViewState.ShowHistory(history.records))
    }

    fun cleanHistory() {
        screenState.postValue(SearchViewState.Loading())
        latestSearchQuery = ""
        searchHistoryInteractor.clearSearchHistory()
        screenState.postValue(SearchViewState.ShowHistory(emptyList()))
    }

    fun search(query: String) {
        latestSearchQuery = query
        if (latestSearchQuery != null && !latestSearchQuery.toString().isEmpty()) {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                setUpSearchRunnable()
            }
        }
    }

    private fun setUpSearchRunnable() {
        screenState.postValue(SearchViewState.Loading())
        viewModelScope.launch {
            searchInteractor.searchTracks(latestSearchQuery.toString()).collect {
                val newState = when (it) {
                    is Resource.ClientError<*> -> SearchViewState.NetworkError()
                    is Resource.ServerError<*> -> SearchViewState.NetworkError()
                    is Resource.Success<*> -> SearchViewState.ShowSearchResult(it.data as List<Track>)
                }
                screenState.postValue(newState)
            }
        }
    }

    private fun setUpDefaultState(): SearchViewState {
        return SearchViewState.ShowHistory(searchHistoryInteractor.getSearchHistory().records)
    }

    override fun onTrackClicked(track: Track) {
        searchHistoryInteractor.addTrackToHistory(track)
    }

    sealed interface SearchViewState {
        class ShowHistory(val tracks: List<Track>) : SearchViewState

        class Loading() : SearchViewState

        class NetworkError() : SearchViewState

        class ShowSearchResult(val tracks: List<Track>) : SearchViewState

        class InitedSearchInput() : SearchViewState
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}