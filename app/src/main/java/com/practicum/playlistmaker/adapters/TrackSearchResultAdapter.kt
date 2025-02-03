package com.practicum.playlistmaker.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.holders.TrackSearchResultViewHolder
import com.practicum.playlistmaker.model.Track

class TrackSearchResultAdapter(val tracks: MutableList<Track> = mutableListOf()) :
    RecyclerView.Adapter<TrackSearchResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackSearchResultViewHolder {
        return TrackSearchResultViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackSearchResultViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    fun updateSearchResult(result: List<Track>) {
        if (tracks.isEmpty()) {
            tracks.addAll(result)
            notifyItemRangeInserted(0, tracks.size)
        } else {
            val size = tracks.size
            tracks.clear()
            tracks.addAll(result)
            notifyItemRangeChanged(0, size)
        }
    }

    fun cleanSearchResult() {
        val size = tracks.size
        tracks.clear()
        this.notifyItemRangeRemoved(0, size)
    }
}