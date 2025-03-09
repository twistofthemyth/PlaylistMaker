package com.practicum.playlistmaker.adapters

import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.practicum.playlistmaker.SearchHistory
import com.practicum.playlistmaker.TrackActivity
import com.practicum.playlistmaker.holders.TrackSearchResultViewHolder
import com.practicum.playlistmaker.model.Track

class TrackSearchResultAdapter(
    private val searchHistory: SearchHistory,
    private val tracks: MutableList<Track> = mutableListOf()
) :
    RecyclerView.Adapter<TrackSearchResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackSearchResultViewHolder {
        return TrackSearchResultViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackSearchResultViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            searchHistory.addTrackToHistory(track)
            val intent = Intent(holder.itemView.context, TrackActivity::class.java)
            intent.putExtra("track", Gson().toJson(track))
            holder.itemView.context.startActivity(intent)
        }
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