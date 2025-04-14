package com.practicum.playlistmaker

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.model.Track

class TrackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)
        val track = Gson().fromJson<Track>(intent.getStringExtra("track"), Track::class.java)

        findViewById<ImageView>(R.id.arrow_back_iv).setOnClickListener {
            finish()
        }

        val albumNameTitle = findViewById<TextView>(R.id.album_name_tv)

        findViewById<TextView>(R.id.album_name_value_tv).apply {
            if (track.collectionName.isNullOrEmpty()) {
                visibility = View.GONE
                albumNameTitle.visibility = View.GONE
            } else {
                text = track.collectionName
            }
        }

        findViewById<ImageView>(R.id.album_iv).apply {
            Glide.with(this.rootView)
                .load(Uri.parse(track.getCoverArtwork()))
                .placeholder(R.drawable.placeholder_album)
                .centerInside()
                .transform(
                    RoundedCorners(resources.getInteger(R.integer.album_image_corner))
                )
                .into(this)
        }

        findViewById<TextView>(R.id.track_name_tv).apply {
            text = track.trackName
        }

        findViewById<TextView>(R.id.author_name_tv).apply {
            text = track.artistName
        }

        findViewById<TextView>(R.id.time_tv).apply {
            text = "0:33"
        }

        findViewById<TextView>(R.id.track_year_value_tv).apply {
            text = track.releaseDate.replaceRange(
                track.releaseDate.indexOf("-"),
                track.releaseDate.length,
                ""
            )
        }

        findViewById<TextView>(R.id.track_genre_value_tv).apply {
            text = track.primaryGenreName
        }

        findViewById<TextView>(R.id.track_country_value_tv).apply {
            text = track.country
        }

        findViewById<TextView>(R.id.track_duration_value_tv).apply {
            text = track.getTrackTime()
        }
    }
}