package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.adapters.TrackSearchResultAdapter
import com.practicum.playlistmaker.util.MockedObjects

class SearchActivity : AppCompatActivity() {

    private var savedInput = ""

    private val mockedSearchResult = MockedObjects.searchResult()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        val clearButton = findViewById<ImageView>(R.id.clear_search_iv)

        val searchInput = findViewById<EditText>(R.id.search_et)
        val rvSearchResult = findViewById<RecyclerView>(R.id.RvSearchResult)
        rvSearchResult.adapter = TrackSearchResultAdapter()
        rvSearchResult.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        searchInput.doOnTextChanged { text, start, before, count ->
            run {
                changeClearButtonVisibility(clearButton, text)
                applyMockedSearchResult(rvSearchResult, text)
                savedInput = text.toString()
            }
        }

        clearButton.setOnClickListener {
            searchInput.text.clear()
            inputMethodManager?.hideSoftInputFromWindow(searchInput.windowToken, 0)
            searchInput.clearFocus()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT_ID, savedInput)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInput = savedInstanceState.getString(SEARCH_TEXT_ID).toString()

        val searchInput = findViewById<EditText>(R.id.search_et)
        searchInput.setText(savedInput)
    }

    private fun changeClearButtonVisibility(button: View, s: CharSequence?) {
        if (s.isNullOrEmpty()) {
            button.visibility = View.GONE
        } else {
            button.visibility = View.VISIBLE
        }
    }

    private fun applyMockedSearchResult(rvSearchResult: RecyclerView, s: CharSequence?) =
        if (s.isNullOrEmpty()) {
            (rvSearchResult.adapter as TrackSearchResultAdapter).cleanSearchResult()
        } else {
            (rvSearchResult.adapter as TrackSearchResultAdapter).updateSearchResult(
                mockedSearchResult
            )
        }

    companion object {
        const val SEARCH_TEXT_ID = "SEARCH_TEXT_ID"
    }
}