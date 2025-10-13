package com.practicum.playlistmaker.media.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asFlow
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.ui.view_model.MediaViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.ui_utils.InfoMessage
import com.practicum.playlistmaker.util.ui_utils.InfoMessageButton
import com.practicum.playlistmaker.util.ui_utils.PlaylistItem
import com.practicum.playlistmaker.util.ui_utils.ProgressBar
import com.practicum.playlistmaker.util.ui_utils.TextScreenTitle
import com.practicum.playlistmaker.util.ui_utils.TextTab
import com.practicum.playlistmaker.util.ui_utils.TrackItems
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
    val titles = listOf(R.string.favorites_tab, R.string.playlists_tab)

    val favoritesState = viewModel.getFavoritesState().asFlow()
        .collectAsState(MediaViewModel.FavoritesState.Empty)
    val playlistState =
        viewModel.getPlaylistState().asFlow().collectAsState(MediaViewModel.PlaylistState.Empty)

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
                0 -> {
                    val favoritesStateValue = favoritesState.value
                    when (favoritesStateValue) {
                        is MediaViewModel.FavoritesState.Content -> FavoritesScreen(
                            favoritesStateValue.favoritesTrack, onClickTrack
                        )

                        MediaViewModel.FavoritesState.Empty -> FavoritesScreen()
                    }
                }

                else -> {
                    val playlistStateValue = playlistState.value
                    when (playlistStateValue) {
                        is MediaViewModel.PlaylistState.Content -> PlaylistsScreen(
                            playlistStateValue.playlists, onCreatePlaylist, onClickPlaylist
                        )

                        MediaViewModel.PlaylistState.Empty -> PlaylistsScreen(onCreatePlaylist = onCreatePlaylist)
                        MediaViewModel.PlaylistState.Loading -> ProgressBar()
                    }
                }
            }
        }
    }
}

@Composable
fun PlaylistsScreen(
    playlists: List<Playlist> = listOf(),
    onCreatePlaylist: () -> Unit = {},
    onClickPlaylist: (Playlist) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
    ) {
        InfoMessageButton(R.string.create_playlist, onClick = onCreatePlaylist)
        if (playlists.isEmpty()) {
            InfoMessage(
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

@Composable
fun FavoritesScreen(list: List<Track> = listOf(), onClickTrack: (Track) -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
    ) {
        if (list.isEmpty()) {
            InfoMessage(
                textId = R.string.empty_favorites,
                imageId = R.drawable.placeholder_not_found
            )
        } else {
            TrackItems(list, onClickTrack)
        }
    }
}