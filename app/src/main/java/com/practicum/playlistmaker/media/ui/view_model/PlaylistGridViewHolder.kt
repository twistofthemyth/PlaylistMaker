package com.practicum.playlistmaker.media.ui.view_model

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistGridItemBinding
import com.practicum.playlistmaker.media.domain.models.Playlist

class PlaylistGridViewHolder(private val binding: PlaylistGridItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Playlist) {
        Glide.with(binding.imageIv)
            .load(data.image?.toUri())
            .placeholder(R.drawable.placeholder_album)
            .centerCrop()
            .transform(
                RoundedCorners(
                    binding.imageIv.context.resources.getInteger(R.integer.album_image_corner)
                )
            )
            .into(binding.imageIv)

        binding.nameTv.text = data.name
        binding.countTv.text = binding.countTv.resources.getQuantityString(
            R.plurals.plular_track,
            data.track.size,
            data.track.size
        )
    }
}