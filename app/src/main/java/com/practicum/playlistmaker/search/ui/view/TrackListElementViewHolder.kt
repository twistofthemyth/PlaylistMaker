package com.practicum.playlistmaker.search.ui.view

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.SearchResultItemBinding
import com.practicum.playlistmaker.search.domain.models.Track

class TrackListElementViewHolder(private val binding: SearchResultItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: Track) {
        Glide.with(itemView)
            .load(data.artwork.toUri())
            .placeholder(R.drawable.placeholder_album)
            .centerInside()
            .transform(
                RoundedCorners(
                    binding.IvAlbum.context.resources.getInteger(R.integer.list_item_image_corner)
                )
            )
            .into(binding.IvAlbum)

        binding.TvTrackAuthor.text = cutIfRequired(data.artistName)
        binding.TvTrackName.text = cutIfRequired(data.trackName)
        binding.TvTrackLength.text = data.trackTime
    }

    private fun cutIfRequired(origin: String): String {
        val maxLength = itemView.context.resources.getInteger(R.integer.list_item_max_length)
        return if (origin.length > maxLength) origin.substring(0, maxLength - 3) + "..." else origin
    }
}