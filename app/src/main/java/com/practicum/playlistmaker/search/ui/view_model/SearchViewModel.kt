package com.practicum.playlistmaker.search.ui.view_model

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.Creator
import com.practicum.playlistmaker.util.domain_utils.Resource

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val searchInteractor = Creator.provideSearchInteractor(getApplication())
    private val searchHistoryInteractor = Creator.provideSearchHistoryInteractor(
        application.getSharedPreferences(
            "SearchViewModel",
            MODE_PRIVATE
        )
    )
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = setUpSearchRunnable()

    private var isClickAllowed = true
    private var latestSearchQuery: String? = null
    private var state = setUpDefaultState()

    fun getState(): LiveData<SearchViewState> = state

    fun repeatSearch() {
        if (latestSearchQuery != null) {
            search(latestSearchQuery.toString())
        }
    }

    fun cleanHistory() {
        state.postValue(SearchViewState.Loading())
        latestSearchQuery = ""
        searchHistoryInteractor.clearSearchHistory()
        state.postValue(SearchViewState.ShowHistory(emptyList()))
    }

    fun clickTrack(track: Track) {
        if (isClickAllowed()) {
            searchHistoryInteractor.addTrackToHistory(track)
            state.postValue(SearchViewState.ProceedToTrack(track))
        }
    }

    fun search(query: String) {
        latestSearchQuery = query
        handler.removeCallbacks(searchRunnable)
        if (latestSearchQuery != null && !latestSearchQuery.toString().isEmpty()) {
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }
    }

    fun setUpSearchRunnable(): Runnable {
        return Runnable {
            state.postValue(SearchViewState.Loading())
            searchInteractor.searchTracks(latestSearchQuery.toString()) {
                val newState = when (it) {
                    is Resource.ClientError<*> -> SearchViewState.NetworkError()
                    is Resource.ServerError<*> -> SearchViewState.NetworkError()
                    is Resource.Success<*> -> SearchViewState.ShowSearchResult(it.data as List<Track>)
                }
                state.postValue(newState);
            }
        }
    }

    private fun setUpDefaultState(): MutableLiveData<SearchViewState> {
        return MutableLiveData<SearchViewState>(SearchViewState.ShowHistory(searchHistoryInteractor.getSearchHistory().records))
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

        class ProceedToTrack(val track: Track) : SearchViewState
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val TOUCH_DEBOUNCE_IN_MILLIS = 1000L
    }
}