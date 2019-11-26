package com.algolia.instantsearch.showcase.search

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.android.hits.HitsArrayAdapter
import com.algolia.instantsearch.helper.android.hits.connectHitsArrayAdapter
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxAutoCompleteTextView
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.SearchMode
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.showcase.configureSearcher
import com.algolia.instantsearch.showcase.list.movie.Movie
import com.algolia.instantsearch.showcase.stubIndex
import com.algolia.search.helper.deserialize
import kotlinx.android.synthetic.main.showcase_search_autocomplete.*


class SearchAutoCompleteTextView : AppCompatActivity() {

    private val searcher = SearcherSingleIndex(stubIndex)
    private val searchBox = SearchBoxConnector(searcher, searchMode = SearchMode.AsYouType)
    private val connection = ConnectionHandler(searchBox)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showcase_search_autocomplete)

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
        val hitsAdapter = HitsArrayAdapter(adapter)
        val searchBoxView = SearchBoxAutoCompleteTextView(autoCompleteTextView)

        autoCompleteTextView.setAdapter(adapter)
        connection += searchBox.connectView(searchBoxView)
        connection += searcher.connectHitsArrayAdapter(hitsAdapter, autoCompleteTextView) { response ->
            response.hits.deserialize(Movie.serializer()).map { it.title }
        }

        configureSearcher(searcher)
        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.disconnect()
    }
}
