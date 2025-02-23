package com.practicum.playlistmaker

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        var searchAdapter = TrackSearchResultAdapter()
        findViewById<RecyclerView>(R.id.RvSearchResult).apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        }

        val searchEditText = findViewById<EditText>(R.id.search_et)
        val errorImage = findViewById<ImageView>(R.id.error_iv)
        val errorText = findViewById<TextView>(R.id.error_tv)
        val errorButton = findViewById<Button>(R.id.error_update_btn)
        val clearButton = findViewById<ImageView>(R.id.clear_search_iv)
        clearButton.setOnClickListener {
            searchEditText.text.clear()
            searchAdapter.cleanSearchResult()
            hideError(errorImage, errorText, errorButton)
        }

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
                            hideError(errorImage, errorText, errorButton)
                            searchAdapter.updateSearchResult(response.body()?.results!!)
                        } else {
                            displayNotFoundError(errorImage, errorText, errorButton)
                        }
                    } else {
                        displayNetworkError(errorImage, errorText, errorButton)
                    }
                }

                override fun onFailure(call: Call<SearchSongResponse>, error: Throwable) {
                    error.printStackTrace()
                    displayNetworkError(errorImage, errorText, errorButton)
                }
            })
        }

        errorButton.setOnClickListener { searchSong.invoke() }

        searchEditText.doOnTextChanged { text, _, _, _ ->
            run {
                hideError(errorImage, errorText, errorButton)
                changeClearButtonVisibility(clearButton, text)
                savedInput = text.toString()
            }
        }
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchSong.invoke()
                true
            }
            false
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

    private fun hideError(errorImage: ImageView, errorText: TextView, errorButton: Button) {
        if (errorImage.visibility != View.GONE || errorText.visibility != View.GONE) {
            errorImage.visibility = View.GONE
            errorText.visibility = View.GONE
            errorButton.visibility = View.GONE
        }
    }

    private fun displayNotFoundError(
        errorImage: ImageView,
        errorText: TextView,
        errorButton: Button
    ) {
        errorImage.setImageResource(R.drawable.placeholder_not_found)
        errorText.setText(R.string.not_found)
        errorImage.visibility = View.VISIBLE
        errorText.visibility = View.VISIBLE
        errorButton.visibility = View.GONE
    }

    private fun displayNetworkError(
        errorImage: ImageView,
        errorText: TextView,
        errorButton: Button
    ) {
        errorImage.setImageResource(R.drawable.placeholder_net_error)
        errorText.setText(R.string.net_error)
        errorImage.visibility = View.VISIBLE
        errorText.visibility = View.VISIBLE
        errorButton.visibility = View.VISIBLE
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