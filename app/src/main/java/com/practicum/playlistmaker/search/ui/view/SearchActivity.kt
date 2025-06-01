package com.practicum.playlistmaker.search.ui.view

import android.content.Intent
import android.os.Bundle
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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.ui.view.TrackActivity
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.practicum.playlistmaker.util.event.SingleLiveEventObserver

class SearchActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var searchAdapter: TrackListAdapter
    private lateinit var searchHistoryAdapter: TrackListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setupRecyclerViews()
        setupSearchInput()
        setupToolbar()
        setupViewModel()
        observeNavigation()
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.resume()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        viewModel.getScreenState().observe(this) { screenState ->
            when (screenState) {
                is SearchViewModel.SearchViewState.Loading -> {
                    showLoading()
                    hideTracks()
                    hideErrors()
                    hideTrackHistory()
                }

                is SearchViewModel.SearchViewState.NetworkError -> {
                    showNetworkError()
                    hideLoading()
                    hideTracks()
                    hideTrackHistory()
                }

                is SearchViewModel.SearchViewState.ShowHistory -> {
                    if (screenState.tracks.isEmpty()) {
                        hideTrackHistory()
                    } else {
                        updateTrackHistory(screenState.tracks)
                        showTrackHistory()
                    }
                    hideLoading()
                    hideErrors()
                    hideTracks()
                    hideClearQueryButton()
                }

                is SearchViewModel.SearchViewState.ShowSearchResult -> {
                    if (screenState.tracks.isEmpty()) {
                        showNotFoundError()
                    } else {
                        showTracks(screenState.tracks)
                    }
                    hideLoading()
                    hideErrors()
                    hideTrackHistory()
                }

                is SearchViewModel.SearchViewState.InitedSearchInput -> {
                    showClearQueryButton()
                }
            }
        }
    }

    private fun setupRecyclerViews() {
        searchAdapter = TrackListAdapter { track -> clickTrack(track) }
        searchHistoryAdapter = TrackListAdapter { track -> clickTrack(track) }

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
                if (text?.isEmpty() != false) {
                    hideClearQueryButton()
                } else {
                    showClearQueryButton()
                }
                viewModel.search(text.toString())
            }

            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    viewModel.search(text.toString())
                }
            }
        }

        findViewById<Button>(R.id.error_update_btn).apply {
            setOnClickListener { viewModel.repeatSearch() }
        }

        findViewById<Button>(R.id.clear_history_btn).apply {
            setOnClickListener { viewModel.cleanHistory() }
        }

        findViewById<ImageView>(R.id.clear_search_iv).apply {
            setOnClickListener { searchInput.setText("") }
        }
    }

    private fun setupToolbar() {
        findViewById<Toolbar>(R.id.toolbar).apply { setNavigationOnClickListener { finish() } }
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

    private fun showTrackHistory() {
        findViewById<EditText>(R.id.search_et).apply {
            setText("")
            clearFocus()
        }
        findViewById<LinearLayout>(R.id.search_history_ll).visibility = View.VISIBLE
    }

    private fun updateTrackHistory(tracks: List<Track>) {
        searchHistoryAdapter.updateList(tracks)
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

    private fun clickTrack(track: Track) {
        viewModel.clickTrack(track)
    }

    private fun observeNavigation() {
        viewModel.getNavigationEvent().observe(this, SingleLiveEventObserver { destination ->
            when (destination) {
                is SearchViewModel.NavigationDestination.ToTrack -> {
                    var intent = Intent(this, TrackActivity::class.java)
                    intent.putExtra("track", Gson().toJson(destination.track))
                    startActivity(intent)
                }
            }
        })
    }

}