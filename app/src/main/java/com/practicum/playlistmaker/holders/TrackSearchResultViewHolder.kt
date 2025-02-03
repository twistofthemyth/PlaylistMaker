package com.practicum.playlistmaker.holders

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.model.Track

class TrackSearchResultViewHolder(parent: ViewGroup) : ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.search_result_item, parent, false)
) {
    fun bind(data: Track) {
        val ivAlbum = itemView.findViewById<ImageView>(R.id.IvAlbum)
        Glide.with(itemView)
            .load(Uri.parse(data.artworkUrl100))
            .placeholder(R.drawable.placeholder_album)
            .centerInside()
            .transform(
                RoundedCorners(
                    ivAlbum.context.resources.getInteger(R.integer.list_item_image_corner)
                )
            )
            .into(ivAlbum)

        val tvTrackAuthor = itemView.findViewById<TextView>(R.id.TvTrackAuthor)
        tvTrackAuthor.text = cutIfRequired(data.artistName)

        val tvTrackName = itemView.findViewById<TextView>(R.id.TvTrackName)
        tvTrackName.text = cutIfRequired(data.trackName)

        val tvTrackLength = itemView.findViewById<TextView>(R.id.TvTrackLength)
        tvTrackLength.text = data.trackTime
    }

    private fun cutIfRequired(origin: String): String {
        val maxLength = itemView.context.resources.getInteger(R.integer.list_item_max_length)
        return if (origin.length > maxLength) origin.substring(0, maxLength - 3) + "..." else origin
    }
}