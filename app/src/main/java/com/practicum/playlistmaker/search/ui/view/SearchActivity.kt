package com.practicum.playlistmaker.search.ui.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.player.ui.view.TrackListAdapter
import com.practicum.playlistmaker.player.ui.view.TrackActivity
import com.practicum.playlistmaker.util.Creator

class SearchActivity : AppCompatActivity() {

    private lateinit var trackInteractor: TrackInteractor

    private lateinit var searchAdapter: TrackListAdapter
    private lateinit var searchHistoryAdapter: TrackListAdapter

    private lateinit var searchQuery: String

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchTracks() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        trackInteractor =
            Creator.provideTrackInteractor(getSharedPreferences("SearchActivity", MODE_PRIVATE))

        setupRecyclerViews()
        setupSearchInput()
        setupToolbar()
        setupSavedSearchQuery()

        onSearchQuery(searchQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        setupSavedSearchQuery()
        onSearchQuery(searchQuery)
    }

    private fun onSearchQuery(query: String) {
        hideErrors()
        hideTracks()
        hideTrackHistory()
        showLoading()
        searchQuery = query
        trackInteractor.saveSearchQuery(query)
        handler.removeCallbacks(searchRunnable)
        if (query.isEmpty()) {
            trackInteractor.getSearchHistory {
                handler.post {
                    hideLoading()
                    if (it.isNotEmpty()) {
                        showTrackHistory(it)
                    } else {
                        hideTrackHistory()
                    }
                }
            }
        } else {
            showClearQueryButton()
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_IN_MILLIS)
        }
    }

    private fun onClearHistoryClicked() {
        trackInteractor.clearHistory()
        onSearchQuery("")
    }

    private fun onTrackClick(track: Track) {
        if (clickDebounce()) {
            trackInteractor.addTrackToHistory(track)
            trackInteractor.getSearchHistory {
                handler.post {
                    searchHistoryAdapter.updateList(it)
                }
            }

            var intent = Intent(this, TrackActivity::class.java)
            intent.putExtra("track", Gson().toJson(track))
            startActivity(intent)
        }
    }

    private fun searchTracks() {
        trackInteractor.searchTracks(
            searchQuery,
            onSuccess = { tracks ->
                handler.post {
                    hideLoading()
                    if (tracks.isEmpty()) {
                        showNotFoundError()
                    } else {
                        showTracks(tracks)
                    }

                }
            },
            onFail = {
                handler.post {
                    hideLoading()
                    showNetworkError()
                }
            }
        )
    }

    private fun setupRecyclerViews() {
        searchAdapter = TrackListAdapter { track -> onTrackClick(track) }
        searchHistoryAdapter = TrackListAdapter { track -> onTrackClick(track) }

        findViewById<RecyclerView>(R.id.RvSearchResult).apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        }

        findViewById<RecyclerView>(R.id.RvSearchHistory).apply {
            adapter = searchHistoryAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setupSearchInput() {
        val searchInput = findViewById<EditText>(R.id.search_et).apply {
            doOnTextChanged { text, _, _, _ ->
                onSearchQuery(text.toString())
            }

            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    onSearchQuery(text.toString())
                }
            }
        }

        findViewById<Button>(R.id.error_update_btn).apply {
            setOnClickListener { onSearchQuery(searchQuery) }
        }

        findViewById<Button>(R.id.clear_history_btn).apply {
            setOnClickListener { onClearHistoryClicked() }
        }

        findViewById<ImageView>(R.id.clear_search_iv).apply {
            setOnClickListener { searchInput.setText("") }
        }
    }

    private fun setupToolbar() {
        findViewById<Toolbar>(R.id.toolbar).apply { setNavigationOnClickListener { finish() } }
    }

    private fun setupSavedSearchQuery() {
        searchQuery = trackInteractor.getSavedSearchQuery()
        if (searchQuery.isNotEmpty()) {
            findViewById<EditText>(R.id.search_et).setText(searchQuery)
            onSearchQuery(searchQuery)
        }
    }

    private fun showLoading() {
        findViewById<ProgressBar>(R.id.search_pb).visibility = View.VISIBLE
    }

    private fun hideLoading() {
        findViewById<ProgressBar>(R.id.search_pb).visibility = View.GONE
    }

    private fun showNetworkError() {
        findViewById<ImageView>(R.id.error_iv).setImageResource(R.drawable.placeholder_net_error)
        findViewById<TextView>(R.id.error_tv).setText(R.string.net_error)
        findViewById<Button>(R.id.error_update_btn).visibility = View.VISIBLE
        findViewById<LinearLayout>(R.id.error_ll).visibility = View.VISIBLE
    }

    private fun showNotFoundError() {
        findViewById<ImageView>(R.id.error_iv).setImageResource(R.drawable.placeholder_not_found)
        findViewById<TextView>(R.id.error_tv).setText(R.string.not_found)
        findViewById<Button>(R.id.error_update_btn).visibility = View.GONE
        findViewById<LinearLayout>(R.id.error_ll).visibility = View.VISIBLE
    }

    private fun hideErrors() {
        findViewById<LinearLayout>(R.id.error_ll).visibility = View.GONE
    }

    private fun showTracks(tracks: List<Track>) {
        searchAdapter.updateList(tracks)
        findViewById<RecyclerView>(R.id.RvSearchResult).visibility = View.VISIBLE
    }

    private fun hideTracks() {
        searchAdapter.updateList(listOf())
        findViewById<RecyclerView>(R.id.RvSearchResult).visibility = View.GONE
    }

    private fun showTrackHistory(tracks: List<Track>) {
        searchHistoryAdapter.updateList(tracks)
        findViewById<LinearLayout>(R.id.search_history_ll).visibility = View.VISIBLE
    }

    private fun hideTrackHistory() {
        findViewById<LinearLayout>(R.id.search_history_ll).visibility = View.GONE
    }

    private fun showClearQueryButton() {
        findViewById<ImageView>(R.id.clear_search_iv).visibility = View.VISIBLE
    }

    private fun hideClearQueryButton() {
        findViewById<ImageView>(R.id.clear_search_iv).visibility = View.GONE
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, TOUCH_DEBOUNCE_IN_MILLIS)
        }
        return current
    }

    companion object {
        const val SEARCH_DEBOUNCE_IN_MILLIS = 2000L
        const val TOUCH_DEBOUNCE_IN_MILLIS = 1000L
    }
}