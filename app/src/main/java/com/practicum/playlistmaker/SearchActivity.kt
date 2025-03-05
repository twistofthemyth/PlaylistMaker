package com.practicum.playlistmaker

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.adapters.TrackSearchHistoryAdapter
import com.practicum.playlistmaker.adapters.TrackSearchResultAdapter
import com.practicum.playlistmaker.model.SearchSongResponse
import com.practicum.playlistmaker.net.ITunesService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private var savedInput = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar).apply { setNavigationOnClickListener { finish() } }

        val searchHistory = SearchHistory((applicationContext as App).getSharedPreferences())
        val searchAdapter = TrackSearchResultAdapter(searchHistory)
        findViewById<RecyclerView>(R.id.RvSearchResult).apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        }

        val searchEditText = findViewById<EditText>(R.id.search_et)
        val errorButton = findViewById<Button>(R.id.error_update_btn)
        val iTunesService = ITunesService()

        val searchSong = fun() {
            searchAdapter.cleanSearchResult()
            iTunesService.api.searchSong(savedInput).enqueue(object : Callback<SearchSongResponse> {
                override fun onResponse(
                    call: Call<SearchSongResponse>,
                    response: Response<SearchSongResponse>
                ) {
                    if (response.code() == 200) {
                        if (response.body()?.results?.isNotEmpty() == true) {
                            hideError()
                            searchAdapter.updateSearchResult(response.body()?.results!!)
                        } else {
                            displayNotFoundError()
                        }
                    } else {
                        displayNetworkError()
                    }
                }

                override fun onFailure(call: Call<SearchSongResponse>, error: Throwable) {
                    error.printStackTrace()
                    displayNetworkError()
                }
            })
        }
        errorButton.setOnClickListener { searchSong.invoke() }

        val searchHistoryAdapter = TrackSearchHistoryAdapter(searchHistory)
        val searchHistoryRv = findViewById<RecyclerView>(R.id.RvSearchHistory)
        searchHistoryRv.adapter = searchHistoryAdapter
        searchHistoryRv.layoutManager =
            LinearLayoutManager(searchHistoryRv.context, LinearLayoutManager.VERTICAL, false)

        val clearButton = findViewById<ImageView>(R.id.clear_search_iv)
        clearButton.setOnClickListener {
            searchEditText.text.clear()
            searchAdapter.cleanSearchResult()
            hideError()
            changeHistoryVisibility(searchHistoryAdapter)
        }

        changeHistoryVisibility(searchHistoryAdapter)

        val clearHistoryButton = findViewById<Button>(R.id.clear_history_btn)
        clearHistoryButton.setOnClickListener {
            searchHistoryAdapter.cleanSearchHistory()
            changeHistoryVisibility(searchHistoryAdapter)
        }

        searchEditText.doOnTextChanged { text, _, _, _ ->
            hideError()
            changeClearButtonVisibility(clearButton, text)
            changeHistoryVisibility(searchHistoryAdapter) { text.isNullOrEmpty() }
            changeSearchResultVisibility { !text.isNullOrEmpty() }
            savedInput = text.toString()
        }

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchSong.invoke()
            }
            false
        }

        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            changeHistoryVisibility(searchHistoryAdapter) { hasFocus }
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

    private fun hideError() {
        findViewById<LinearLayout>(R.id.error_ll).visibility = View.GONE
    }

    private fun displayNotFoundError() {
        findViewById<ImageView>(R.id.error_iv).setImageResource(R.drawable.placeholder_not_found)
        findViewById<TextView>(R.id.error_tv).setText(R.string.not_found)
        findViewById<Button>(R.id.error_update_btn).visibility = View.GONE
        findViewById<LinearLayout>(R.id.error_ll).visibility = View.VISIBLE
    }

    private fun displayNetworkError() {
        findViewById<ImageView>(R.id.error_iv).setImageResource(R.drawable.placeholder_net_error)
        findViewById<TextView>(R.id.error_tv).setText(R.string.net_error)
        findViewById<Button>(R.id.error_update_btn).visibility = View.VISIBLE
        findViewById<LinearLayout>(R.id.error_ll).visibility = View.VISIBLE
    }

    private fun changeClearButtonVisibility(button: View, s: CharSequence?) {
        if (s.isNullOrEmpty()) {
            button.visibility = View.GONE
        } else {
            button.visibility = View.VISIBLE
        }
    }

    private fun changeHistoryVisibility(adapter: TrackSearchHistoryAdapter) {
        changeHistoryVisibility(adapter) { true }
    }

    private fun changeHistoryVisibility(
        adapter: TrackSearchHistoryAdapter,
        displayCondition: () -> Boolean
    ) {
        val searchHistoryLl = findViewById<LinearLayout>(R.id.search_history_ll)
        if (!adapter.isEmpty() && displayCondition.invoke()) {
            adapter.notifyItemRangeChanged(0, adapter.itemCount)
            searchHistoryLl.visibility = View.VISIBLE
        } else {
            searchHistoryLl.visibility = View.GONE
        }
    }

    private fun changeSearchResultVisibility(displayCondition: () -> Boolean) {
        val searchResultRv = findViewById<RecyclerView>(R.id.RvSearchResult)
        if(displayCondition.invoke()) {
            searchResultRv.visibility = View.VISIBLE
        } else {
            searchResultRv.visibility = View.GONE
        }
    }

    companion object {
        const val SEARCH_TEXT_ID = "SEARCH_TEXT_ID"
    }
}