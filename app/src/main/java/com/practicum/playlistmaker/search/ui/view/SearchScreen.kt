package com.practicum.playlistmaker.search.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asFlow
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.util.ui_utils.InfoMessage
import com.practicum.playlistmaker.util.ui_utils.InfoMessageButton
import com.practicum.playlistmaker.util.ui_utils.ProgressBar
import com.practicum.playlistmaker.util.ui_utils.SearchEditText
import com.practicum.playlistmaker.util.ui_utils.TextScreenTitle
import com.practicum.playlistmaker.util.ui_utils.TrackItems

@Composable
fun SearchScreen(viewModel: SearchViewModel) {
    val state = viewModel.getScreenState().asFlow()
        .collectAsState(SearchViewModel.SearchViewState.Loading()).value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
    ) {
        TextScreenTitle(R.string.search_button_txt)
        SearchEditText(
            onValueChange = { viewModel.search(it) },
            onValueClean = { viewModel.cleanSearchQuery() })
        when (state) {
            is SearchViewModel.SearchViewState.Loading -> ProgressBar()
            is SearchViewModel.SearchViewState.NetworkError -> NetworkErrorScreen(viewModel)
            is SearchViewModel.SearchViewState.ShowHistory -> HistoryScreen(viewModel, state.tracks)
            is SearchViewModel.SearchViewState.ShowSearchResult -> SearchResultScreen(
                viewModel,
                state.tracks
            )
        }
    }
}

@Composable
private fun NetworkErrorScreen(viewModel: SearchViewModel) {
    InfoMessage(
        modifier = Modifier.padding(top = 102.dp),
        textId = R.string.net_error,
        descriptionId = R.string.net_error_description,
        imageId = R.drawable.placeholder_net_error,
        buttonTextId = R.string.search_query_update_button,
        onButtonClickAction = { viewModel.repeatSearch() }
    )
}

@Composable
private fun HistoryScreen(viewModel: SearchViewModel, tracks: List<Track>) {
    if (tracks.isNotEmpty()) {
        InfoMessage(modifier = Modifier.padding(top = 42.dp), textId = R.string.you_searched)
        TrackItems(
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
            tracks = tracks,
            onClick = { viewModel.clickTrack(it) }
        )
        InfoMessageButton(R.string.clear_search_history_button) {
            viewModel.cleanHistory()
        }
    }
}

@Composable
private fun SearchResultScreen(viewModel: SearchViewModel, tracks: List<Track>) {
    if (tracks.isEmpty()) {
        InfoMessage(
            modifier = Modifier.padding(top = 102.dp),
            textId = R.string.not_found,
            imageId = R.drawable.placeholder_not_found
        )
    }
    TrackItems(
        modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp),
        tracks = tracks,
        onClick = { viewModel.clickTrack(it) }
    )
}