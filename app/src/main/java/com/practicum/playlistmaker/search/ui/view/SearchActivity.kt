package com.practicum.playlistmaker.search.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.player.ui.view.TrackActivity
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.util.event.SingleLiveEventObserver
import org.koin.android.ext.android.inject

class SearchActivity : AppCompatActivity() {

    private val viewModel: SearchViewModel by inject()
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchAdapter: TrackListAdapter
    private lateinit var searchHistoryAdapter: TrackListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupSearchInput()
        setupRecyclerViews()
        setupViewModel()
        observeNavigation()
    }


    private fun setupViewModel() {
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
                    binding.searchEt.apply {
                        setText("")
                        hideKeyboard(this)
                    }
                    hideTracks()
                    hideErrors()
                    if (screenState.tracks.isEmpty()) {
                        hideTrackHistory()
                    } else {
                        updateTrackHistory(screenState.tracks)
                        showTrackHistory()
                    }
                }

                is SearchViewModel.SearchViewState.ShowSearchResult -> {
                    hideLoading()
                    hideErrors()
                    hideTrackHistory()
                    if (screenState.tracks.isEmpty()) {
                        showNotFoundError()
                    } else {
                        showTracks(screenState.tracks)
                    }
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

        binding.RvSearchResult.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        }

        binding.RvSearchHistory.apply {
            adapter = searchHistoryAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setupSearchInput() {
        binding.searchEt.apply {
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

        binding.errorUpdateBtn.setOnClickListener { viewModel.repeatSearch() }
        binding.clearHistoryBtn.setOnClickListener { viewModel.cleanHistory() }
        binding.clearSearchIv.setOnClickListener { viewModel.cleanSearchQuery() }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun showLoading() {
        binding.searchPb.isVisible = true
    }

    private fun hideLoading() {
        binding.searchPb.isVisible = false
    }

    private fun showNetworkError() {
        binding.errorIv.setImageResource(R.drawable.placeholder_net_error)
        binding.errorTv.setText(R.string.net_error)
        binding.errorUpdateBtn.isVisible = true
        binding.errorLl.isVisible = true
    }

    private fun showNotFoundError() {
        binding.errorIv.setImageResource(R.drawable.placeholder_not_found)
        binding.errorTv.setText(R.string.not_found)
        binding.errorUpdateBtn.isVisible = false
        binding.errorLl.isVisible = true
    }

    private fun hideErrors() {
        binding.errorLl.isVisible = false
    }

    private fun showTracks(tracks: List<Track>) {
        searchAdapter.updateList(tracks)
        binding.RvSearchResult.isVisible = true
    }

    private fun hideTracks() {
        searchAdapter.updateList(listOf())
        binding.RvSearchResult.isVisible = false
    }

    private fun showTrackHistory() {
        binding.searchEt.apply {
            setText("")
            clearFocus()
        }
        binding.searchHistoryLl.isVisible = true
    }

    private fun updateTrackHistory(tracks: List<Track>) {
        searchHistoryAdapter.updateList(tracks)
    }

    private fun hideTrackHistory() {
        binding.searchHistoryLl.isVisible = false
    }

    private fun showClearQueryButton() {
        binding.clearSearchIv.isVisible = true
    }

    private fun hideClearQueryButton() {
        binding.clearSearchIv.isVisible = false
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

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}