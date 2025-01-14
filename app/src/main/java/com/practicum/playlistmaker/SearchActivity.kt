package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchInput = findViewById<EditText>(R.id.search_et)
        val clearButton = findViewById<ImageView>(R.id.clear_search_iv)
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                changeClearButtonVisibility(clearButton, s)
            }

            override fun afterTextChanged(s: Editable?) {
                // do nothing
            }
        }

        searchInput.addTextChangedListener(searchTextWatcher)
        clearButton.setOnClickListener {
            searchInput.text.clear()
            inputMethodManager?.hideSoftInputFromWindow(searchInput.windowToken, 0)
            searchInput.clearFocus()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val searchInput = findViewById<EditText>(R.id.search_et)
        if (!searchInput.text.isNullOrEmpty()) {
            outState.putString(SEARCH_TEXT_ID, searchInput.text.toString())
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedText = savedInstanceState.getString(SEARCH_TEXT_ID)
        val searchInput = findViewById<EditText>(R.id.search_et)
        searchInput.setText(savedText)
    }

    private fun changeClearButtonVisibility(button: View, s: CharSequence?) {
        if (s.isNullOrEmpty()) {
            button.visibility = View.GONE
        } else {
            button.visibility = View.VISIBLE
        }
    }

    companion object {
        const val SEARCH_TEXT_ID = "SEARCH_TEXT_ID"
    }
}