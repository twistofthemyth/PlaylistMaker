package com.practicum.playlistmaker.media.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asFlow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.ui.view_model.MediaViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.ui_utils.compose.InfoMessage
import com.practicum.playlistmaker.util.ui_utils.compose.InfoMessageButton
import com.practicum.playlistmaker.search.ui.compose.TextScreenTitle
import com.practicum.playlistmaker.search.ui.compose.TrackItems
import com.practicum.playlistmaker.util.ui_utils.compose.PlaylistItem
import com.practicum.playlistmaker.util.ui_utils.compose.ProgressBar
import com.practicum.playlistmaker.util.ui_utils.compose.TextTab
import kotlinx.coroutines.launch

@Composable
fun MediaScreen(
    viewModel: MediaViewModel,
    onClickTrack: (Track) -> Unit = {},
    onCreatePlaylist: () -> Unit = {},
    onClickPlaylist: (Playlist) -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val titles = remember { listOf(R.string.favorites_tab, R.string.playlists_tab) }

    val favoritesState = viewModel.getFavoritesState().asFlow()
        .collectAsStateWithLifecycle(MediaViewModel.FavoritesState.Empty)
    val playlistState = viewModel.getPlaylistState().asFlow()
        .collectAsStateWithLifecycle(MediaViewModel.PlaylistState.Empty)

    Column(modifier = Modifier.fillMaxSize()) {
        TextScreenTitle(R.string.media_button_txt)
        SecondaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.Transparent,
            indicator = {
                TabRowDefaults.SecondaryIndicator(
                    color = colorResource(R.color.text_color),
                    height = 2.dp,
                    modifier = Modifier.tabIndicatorOffset(pagerState.currentPage)
                )
            }
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    text = { TextTab(text = stringResource(title)) },
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    })
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (it) {
                0 -> FavoritesScreen(
                    state = favoritesState.value,
                    onClickTrack = onClickTrack
                )

                else -> {
                    when (playlistState.value) {
                        is MediaViewModel.PlaylistState.Loading -> ProgressBar()
                        else -> PlaylistsScreen(
                            state = playlistState.value,
                            onCreatePlaylist = onCreatePlaylist,
                            onClickPlaylist = onClickPlaylist
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoritesScreen(
    state: MediaViewModel.FavoritesState,
    onClickTrack: (Track) -> Unit = {}
) {
    val trackList = when (state) {
        is MediaViewModel.FavoritesState.Content -> state.favoritesTrack
        else -> emptyList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
    ) {
        if (trackList.isEmpty()) {
            InfoMessage(
                modifier = Modifier.padding(top = 102.dp),
                textId = R.string.empty_favorites,
                imageId = R.drawable.placeholder_not_found
            )
        } else {
            TrackItems(
                modifier = Modifier.padding(top = 20.dp, start = 16.dp, end = 16.dp),
                tracks = trackList,
                onClick = onClickTrack
            )
        }
    }
}

@Composable
private fun PlaylistsScreen(
    state: MediaViewModel.PlaylistState,
    onCreatePlaylist: () -> Unit = {},
    onClickPlaylist: (Playlist) -> Unit = {}
) {
    val playlists = when (state) {
        is MediaViewModel.PlaylistState.Content -> state.playlists
        else -> emptyList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
    ) {
        InfoMessageButton(R.string.create_playlist, onClick = onCreatePlaylist)
        if (playlists.isEmpty()) {
            InfoMessage(
                modifier = Modifier.padding(top = 36.dp),
                textId = R.string.empty_playlists,
                imageId = R.drawable.placeholder_not_found
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(playlists) { item ->
                    PlaylistItem(item, onClickPlaylist)
                }
            }
        }
    }
}