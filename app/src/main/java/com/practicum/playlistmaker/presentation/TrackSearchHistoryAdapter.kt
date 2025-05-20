package com.practicum.playlistmaker.presentation

import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.practicum.playlistmaker.domain.api.TrackInteractor
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.TrackActivity
import java.util.concurrent.atomic.AtomicReference

class TrackSearchHistoryAdapter(private val searchHistory: TrackInteractor) :
    RecyclerView.Adapter<TrackSearchResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackSearchResultViewHolder {
        return TrackSearchResultViewHolder(parent)
    }

    override fun getItemCount(): Int {
        val count: AtomicReference<Int> = AtomicReference()
        searchHistory.getHistorySize { count.set(it) }
        return count.get()
    }

    override fun onBindViewHolder(holder: TrackSearchResultViewHolder, position: Int) {
        val track = AtomicReference<Track>()
        val historySize = AtomicReference<Int>()
        searchHistory.getTrackInHistory(position) { track.set(it) }
        searchHistory.getHistorySize { historySize.set(it) }

        holder.bind(track.get())
        holder.itemView.setOnClickListener {
            searchHistory.addTrackToHistory(track.get())
            notifyItemRangeChanged(0, historySize.get())
            val intent = Intent(holder.itemView.context, TrackActivity::class.java)
            intent.putExtra("track", Gson().toJson(track))
            holder.itemView.context.startActivity(intent)
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