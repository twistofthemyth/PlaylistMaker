package com.practicum.playlistmaker.media.ui.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MediaViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FavoritesTrackFragment()
            1 -> PlaylistsFragment()
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int = 2
}