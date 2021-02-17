package com.algolia.instantsearch.showcase.dynamic

import android.os.Bundle
import android.view.View
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
import com.algolia.instantsearch.showcase.configureToolbar
import com.algolia.instantsearch.showcase.dynamic.components.DynamicSortBanner
import com.algolia.instantsearch.showcase.dynamic.components.DynamicSortViewModel
import com.algolia.instantsearch.showcase.dynamic.components.connectSearcher
import com.algolia.instantsearch.showcase.dynamic.components.connectView
import com.algolia.instantsearch.showcase.list.product.Product
import com.algolia.instantsearch.showcase.list.product.ProductAdapter
import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.helper.deserialize
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query
import io.ktor.client.features.logging.LogLevel
import kotlinx.android.synthetic.main.include_search.*
import kotlinx.android.synthetic.main.include_search_hint.*
import kotlinx.android.synthetic.main.showcase_search.*
import kotlinx.android.synthetic.main.showcase_search.hits
import kotlinx.android.synthetic.main.showcase_search.toolbar
import kotlinx.android.synthetic.main.showcase_search_toggle.*

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
    private val searcher = SearcherSingleIndex(index, Query())
    private val searchBox = SearchBoxConnector(searcher, searchMode = SearchMode.OnSubmit)
    private val connection = ConnectionHandler(searchBox)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showcase_search_toggle)

        val adapter = ProductAdapter()
        val searchBoxView = SearchBoxViewAppCompat(searchView)

        connection += searchBox.connectView(searchBoxView)
        connection += searcher.connectHitsView(adapter) { response ->
            response.hits.deserialize(Product.serializer())
        }

        val viewModel = DynamicSortViewModel()
        connection += viewModel.connectSearcher(searcher)

        val view = DynamicSortBanner(hintBanner, hintButton, hintLabel)
        connection += viewModel.connectView(view)

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
