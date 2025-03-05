package com.practicum.playlistmaker.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.SearchHistory
import com.practicum.playlistmaker.holders.TrackSearchResultViewHolder

class TrackSearchHistoryAdapter(private val searchHistory: SearchHistory) :
    RecyclerView.Adapter<TrackSearchResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackSearchResultViewHolder {
        return TrackSearchResultViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return searchHistory.getHistorySize()
    }

    override fun onBindViewHolder(holder: TrackSearchResultViewHolder, position: Int) {
        val track = searchHistory.getTrackInHistory(position)
        holder.bind(track)
        holder.itemView.setOnClickListener {
            searchHistory.addTrackToHistory(track)
            notifyItemRangeInserted(searchHistory.getHistorySize()-1, searchHistory.getHistorySize())
        }
    }

    fun cleanSearchHistory() {
        val size = searchHistory.getHistorySize()
        searchHistory.clearHistory()
        this.notifyItemRangeRemoved(0, size)
    }

    fun isEmpty(): Boolean {
        return searchHistory.getHistorySize() == 0
    }
}