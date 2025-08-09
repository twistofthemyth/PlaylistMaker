package com.practicum.playlistmaker.media.ui.view_model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.SearchResultItemBinding
import com.practicum.playlistmaker.media.domain.models.Playlist

class PlaylistAdapter(private val onPlaylistClicked: (Playlist) -> Unit) :
    RecyclerView.Adapter<PlaylistViewHolder>() {

    private val cachedList: MutableList<Playlist> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistViewHolder {
        val binding = SearchResultItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PlaylistViewHolder,
        position: Int
    ) {
        val playlist = cachedList[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener { onPlaylistClicked.invoke(playlist) }
    }

    override fun getItemCount(): Int {
        return cachedList.size
    }

    fun updateList(result: List<Playlist>) {
        var oldSize = cachedList.size
        cachedList.clear()
        notifyItemRangeRemoved(0, oldSize)
        cachedList.addAll(result)
        notifyItemRangeInserted(0, result.size)
    }
}