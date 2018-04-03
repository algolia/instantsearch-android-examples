package com.algolia.instantsearch.examples.querysuggestions

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.algolia.instantsearch.helpers.InstantSearch
import com.algolia.instantsearch.helpers.Searcher
import com.algolia.instantsearch.model.SearchBoxViewModel
import com.algolia.instantsearch.ui.views.Hits
import com.algolia.instantsearch.ui.views.SearchBox

class QuerySuggestionsActivity : AppCompatActivity() {
    companion object {
        internal const val ALGOLIA_APP_ID = "latency"
        internal const val ALGOLIA_INDEX_PRODUCTS = "instant_search"
        internal const val ALGOLIA_INDEX_SUGGESTIONS = "instantsearch_query_suggestions"
        internal const val ALGOLIA_API_KEY = "afc3dd66dd1293e2e2736a5a51b05c0a"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suggestions)

        val searcherProducts = Searcher.create(ALGOLIA_APP_ID, ALGOLIA_API_KEY, ALGOLIA_INDEX_PRODUCTS, "hits")
        val searcherSuggestions = Searcher.create(ALGOLIA_APP_ID, ALGOLIA_API_KEY, ALGOLIA_INDEX_SUGGESTIONS, "suggestions")
        val searchBox = SearchBoxViewModel(findViewById<SearchBox>(R.id.searchBox))
        InstantSearch(this, searcherProducts).registerSearchView(this, searchBox)
        InstantSearch(this, searcherSuggestions).registerSearchView(this, searchBox)

        findViewById<Hits>(R.id.suggestions).setOnItemClickListener({ _: RecyclerView?, _: Int, v: View? ->
            searchBox.searchView.setQuery(v!!.findViewById<TextView>(R.id.suggestion_query).text.toString(), true)
        })

        searcherProducts.search()
        searcherSuggestions.search()
    }
}