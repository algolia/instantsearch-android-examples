package com.algolia.instantsearch.examples.querysuggestions

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.algolia.instantsearch.helpers.InstantSearch
import com.algolia.instantsearch.helpers.Searcher
import com.algolia.instantsearch.model.SearchBoxViewModel
import com.algolia.instantsearch.ui.views.SearchBox

class QuerySuggestionsActivity : AppCompatActivity() {
    companion object {
        internal const val ALGOLIA_APP_ID = "latency"
        internal const val ALGOLIA_INDEX_PRODUCTS = "instant_search"
        internal const val ALGOLIA_INDEX_SUGGESTIONS = "instantsearch_query_suggestions"
        internal const val ALGOLIA_API_KEY = "afc3dd66dd1293e2e2736a5a51b05c0a"
    }

    private lateinit var searcherProducts: Searcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suggestions)

        searcherProducts = Searcher.create(ALGOLIA_APP_ID, ALGOLIA_API_KEY, ALGOLIA_INDEX_PRODUCTS, "hits")
        val searchBox = SearchBoxViewModel(findViewById<SearchBox>(R.id.searchBox))
        InstantSearch(this, searcherProducts).registerSearchView(this, searchBox)
        searcherProducts.search()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        searcherProducts.search(intent)
    }
}