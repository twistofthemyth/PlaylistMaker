package com.practicum.playlistmaker.presentation

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.practicum.playlistmaker.data.SharedPrefClient
import com.practicum.playlistmaker.domain.api.TrackInteractor
import com.practicum.playlistmaker.ui.TrackActivity
import com.practicum.playlistmaker.domain.models.Track

class TrackSearchResultAdapter(
    private val searchHistory: TrackInteractor,
    private val tracks: MutableList<Track> = mutableListOf()
) :
    RecyclerView.Adapter<TrackSearchResultViewHolder>() {

    companion object {
        const val TOUCH_DEBOUNCE_IN_MILLIS = 1000L
    }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

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
            if (clickDebounce()) {
                searchHistory.addTrackToHistory(track)
                val intent = Intent(holder.itemView.context, TrackActivity::class.java)
                intent.putExtra("track", Gson().toJson(track))
                holder.itemView.context.startActivity(intent)
            }
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

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, TOUCH_DEBOUNCE_IN_MILLIS)
        }
        return current
    }
}