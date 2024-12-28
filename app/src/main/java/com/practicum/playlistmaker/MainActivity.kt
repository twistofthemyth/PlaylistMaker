package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search_button)
        val mediaButton = findViewById<Button>(R.id.media_button)
        val settingsButton = findViewById<Button>(R.id.settings_button)

        //TODO Убрать после прохождения ревью
//        val searchButtonClickListener = object : View.OnClickListener {
//            override fun onClick(v: View?) {
//                Toast.makeText(this@MainActivity, "Нажата кнопка Поиск!!", Toast.LENGTH_LONG).show()
//            }
//        }
//        searchButton.setOnClickListener(searchButtonClickListener)
//
//
//        mediaButton.setOnClickListener {
//            Toast.makeText(this@MainActivity, "Нажата кнопка Медиатека!!", Toast.LENGTH_LONG).show()
//        }
//
//        settingsButton.setOnClickListener {
//            Toast.makeText(this@MainActivity, "Нажата кнопка Настройки!!", Toast.LENGTH_SHORT)
//                .show()
//        }

        searchButton.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        mediaButton.setOnClickListener {
            startActivity(Intent(this, MediaActivity::class.java))
        }

        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}