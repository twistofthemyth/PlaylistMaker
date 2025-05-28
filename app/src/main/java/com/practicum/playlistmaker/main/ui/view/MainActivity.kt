package com.practicum.playlistmaker.main.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.ui.view.MediaActivity
import com.practicum.playlistmaker.search.ui.view.SearchActivity
import com.practicum.playlistmaker.settings.ui.view.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupInput()
    }

    private fun setupInput() {
        findViewById<Button>(R.id.search_button).apply {
            setOnClickListener {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
            }
        }

        findViewById<Button>(R.id.media_button).apply {
            setOnClickListener {
                startActivity(Intent(this@MainActivity, MediaActivity::class.java))
            }
        }

        findViewById<Button>(R.id.settings_button).apply {
            setOnClickListener {
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
            }
        }
    }
}