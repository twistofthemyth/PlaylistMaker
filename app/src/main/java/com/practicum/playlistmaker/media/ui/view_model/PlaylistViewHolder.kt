package com.practicum.playlistmaker.media.ui.view_model

import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.SearchResultItemBinding
import com.practicum.playlistmaker.media.domain.models.Playlist

class PlaylistViewHolder(private val binding: SearchResultItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Playlist) {
        Glide.with(itemView)
            .load(data.image.toUri())
            .placeholder(R.drawable.placeholder_album)
            .centerCrop()
            .transform(
                RoundedCorners(
                    binding.IvAlbum.context.resources.getInteger(R.integer.album_image_corner)
                )
            )
            .into(binding.IvAlbum)

        binding.TvTrackName.text = data.name
        binding.TvTrackAuthor.text = binding.TvTrackAuthor.resources.getQuantityString(
            R.plurals.plular_track,
            data.track.size,
            data.track.size
        )
        binding.TvTrackLength.isVisible = false
        binding.IvDotDelimiter.isVisible = false
    }
}