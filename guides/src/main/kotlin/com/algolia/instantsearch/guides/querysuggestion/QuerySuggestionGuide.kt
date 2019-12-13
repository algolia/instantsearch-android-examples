package com.algolia.instantsearch.guides.querysuggestion

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.searcher.connectView
import com.algolia.instantsearch.guides.R
import com.algolia.instantsearch.helper.android.list.autoScrollToStart
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.search.client.ClientSearch
import com.algolia.search.helper.deserialize
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.search.Query
import io.ktor.client.features.logging.LogLevel
import kotlinx.android.synthetic.main.activity_places.searchView
import kotlinx.android.synthetic.main.activity_query_suggestion.*


class QuerySuggestionGuide : AppCompatActivity() {

    val client = ClientSearch(ApplicationID("latency"), APIKey("1f6fd3a6fb973cb08419fe7d288fa4db"), LogLevel.ALL)
    val searcher = SearcherMultipleIndex(
        client,
        listOf(
            IndexQuery(IndexName("query_suggestions"), Query(hitsPerPage = 3)),
            IndexQuery(IndexName("bestbuy_promo"))
        )
    )
    val adapter = Adapter()
    val searchBox = SearchBoxConnector(searcher)
    val connection = ConnectionHandler(searchBox)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query_suggestion)

        val searchBoxView = SearchBoxViewAppCompat(searchView)

        connection += searcher.connectView(adapter::submitList) { response ->
            if (response != null) {
                mutableListOf<AdapterItem>().apply {
                    this += AdapterItem.Header("Suggestions")
                    this += response.results[0].hits.deserialize(AdapterItem.Suggestion.serializer()).apply {
                        forEach { suggestion ->
                            suggestion.onClickListener = View.OnClickListener {
                                searchBoxView.setText(suggestion.query)
                            }
                        }
                    }
                    this += AdapterItem.Header("Results")
                    this += response.results[1].hits.deserialize(AdapterItem.Product.serializer())
                }
            } else emptyList()
        }
        connection += searchBox.connectView(searchBoxView)

        list.let {
            it.itemAnimator = null
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(this)
            it.autoScrollToStart(adapter)
        }

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}