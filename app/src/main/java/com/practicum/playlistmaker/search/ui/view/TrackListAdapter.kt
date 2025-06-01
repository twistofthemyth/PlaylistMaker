package com.practicum.playlistmaker.search.ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.SearchResultItemBinding
import com.practicum.playlistmaker.search.domain.models.Track

class TrackListAdapter(
    private val onTrackClicked: (Track) -> Unit
) :
    RecyclerView.Adapter<TrackListElementViewHolder>() {

    private val cachedList: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListElementViewHolder {
        val binding = SearchResultItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TrackListElementViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cachedList.size
    }

    override fun onBindViewHolder(holder: TrackListElementViewHolder, position: Int) {
        val track = cachedList[position]
        holder.bind(track)
        holder.itemView.setOnClickListener { onTrackClicked.invoke(track) }
    }

    fun updateList(result: List<Track>) {
        var oldSize = cachedList.size
        cachedList.clear()
        notifyItemRangeRemoved(0, oldSize)
        cachedList.addAll(result)
        notifyItemRangeInserted(0, result.size)
    }
}