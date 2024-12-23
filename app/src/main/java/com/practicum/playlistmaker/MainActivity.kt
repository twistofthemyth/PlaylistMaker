package com.practicum.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val searchButton = findViewById<Button>(R.id.search_button)
        val searchButtonClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Нажата кнопка Поиск!!", Toast.LENGTH_LONG).show()
            }
        }
        searchButton.setOnClickListener(searchButtonClickListener)

        val mediaButton = findViewById<Button>(R.id.media_button)
        mediaButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажата кнопка Медиатека!!", Toast.LENGTH_LONG).show()
        }

        attachDebugToast(findViewById<Button>(R.id.settings_button))
    }

    private fun attachDebugToast(view: TextView) {
        view.setOnClickListener({
            Toast.makeText(
                this@MainActivity,
                "Произошло нажатие на ${view.text}!!",
                Toast.LENGTH_SHORT
            ).show()
        })
    }
}