package com.practicum.playlistmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.domain_utils.Resource
import com.practicum.playlistmaker.util.event.Event

class SearchViewModel(
    val searchInteractor: SearchInteractor,
    val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = setUpSearchRunnable()
    private val screenState = MutableLiveData(setUpDefaultState())
    private val navigationEvent = MutableLiveData<Event<NavigationDestination>>()

    private var isClickAllowed = true
    private var latestSearchQuery: String? = null

    fun getScreenState(): LiveData<SearchViewState> = screenState
    fun getNavigationEvent(): LiveData<Event<NavigationDestination>> = navigationEvent

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

    fun clickTrack(track: Track) {
        if (isClickAllowed()) {
            searchHistoryInteractor.addTrackToHistory(track)
            navigationEvent.value = Event(NavigationDestination.ToTrack(track))
        }
    }

    fun search(query: String) {
        latestSearchQuery = query
        handler.removeCallbacks(searchRunnable)
        if (latestSearchQuery != null && !latestSearchQuery.toString().isEmpty()) {
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }
    }

    private fun setUpSearchRunnable(): Runnable {
        return Runnable {
            screenState.postValue(SearchViewState.Loading())
            searchInteractor.searchTracks(latestSearchQuery.toString()) {
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

    private fun isClickAllowed(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed(
                { isClickAllowed = true },
                TOUCH_DEBOUNCE_IN_MILLIS
            )
        }
        return current
    }

    sealed interface SearchViewState {
        class ShowHistory(val tracks: List<Track>) : SearchViewState

        class Loading() : SearchViewState

        class NetworkError() : SearchViewState

        class ShowSearchResult(val tracks: List<Track>) : SearchViewState

        class InitedSearchInput() : SearchViewState
    }

    sealed interface NavigationDestination {
        class ToTrack(val track: Track) : NavigationDestination
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val TOUCH_DEBOUNCE_IN_MILLIS = 1000L
    }
}