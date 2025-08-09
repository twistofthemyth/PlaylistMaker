package com.practicum.playlistmaker.media.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMediaBinding

class MediaFragment : Fragment() {
    private val args by navArgs<MediaFragmentArgs>()
    private var _binding: FragmentMediaBinding? = null
    private val binding get() = _binding!!

    private var _tabMediator: TabLayoutMediator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMediaBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = MediaViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        _tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = resources.getString(R.string.favorites_tab)
                1 -> tab.text = resources.getString(R.string.playlists_tab)
            }
        }.apply {
            attach()
            binding.viewPager.post {
                binding.viewPager.currentItem = args.tabId
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _tabMediator?.detach()
        _binding = null
        _tabMediator = null
    }
}