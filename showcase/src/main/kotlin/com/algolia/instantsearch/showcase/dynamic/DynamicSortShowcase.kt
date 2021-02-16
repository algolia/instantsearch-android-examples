package com.algolia.instantsearch.showcase.dynamic

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.SearchMode
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.showcase.configureRecyclerView
import com.algolia.instantsearch.showcase.configureSearchView
import com.algolia.instantsearch.showcase.configureSearcher
import com.algolia.instantsearch.showcase.configureToolbar
import com.algolia.instantsearch.showcase.list.product.Product
import com.algolia.instantsearch.showcase.list.product.ProductAdapter
import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.helper.deserialize
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import io.ktor.client.features.logging.LogLevel
import kotlinx.android.synthetic.main.include_search.*
import kotlinx.android.synthetic.main.showcase_search.*

class DynamicSortShowcase : AppCompatActivity() {

    private val client = ClientSearch(
        ConfigurationSearch(
            ApplicationID("C7RIRJRYR9"),
            APIKey("77af6d5ffb27caa5ff4937099fcb92e8"),
            logLevel = LogLevel.ALL
        )
    )

    // Most relevant: test_Bestbuy
    // > Smart sort (lowest price): test_Bestbuy_vr_price_asc
    // Hard sort (lowest price) test_Bestbuy_replica_price_asc
    private val index = client.initIndex(IndexName("test_Bestbuy_vr_price_asc"))
    private val searcher = SearcherSingleIndex(index)
    private val searchBox = SearchBoxConnector(searcher, searchMode = SearchMode.OnSubmit)
    private val connection = ConnectionHandler(searchBox)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showcase_search)

        val adapter = ProductAdapter()
        val searchBoxView = SearchBoxViewAppCompat(searchView)

        connection += searchBox.connectView(searchBoxView)
        connection += searcher.connectHitsView(adapter) { response ->
            response.hits.deserialize(Product.serializer())
        }
        
        configureToolbar(toolbar)
        configureRecyclerView(hits, adapter)
        configureSearchView(searchView, getString(R.string.search_products))

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
