package com.practicum.playlistmaker.media.ui.view_model

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistGridItemBinding
import com.practicum.playlistmaker.media.domain.models.Playlist

class PlaylistViewHolder(private val binding: PlaylistGridItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Playlist) {
        Glide.with(itemView)
            .load(data.image.toUri())
            .placeholder(R.drawable.placeholder_album)
            .centerInside()
            .transform(
                RoundedCorners(
                    binding.imageIv.context.resources.getInteger(R.integer.album_image_corner)
                )
            )
            .into(binding.imageIv)

        binding.nameTv.text = data.name
        binding.countTv.text = "${data.track.size} треков"
    }
}