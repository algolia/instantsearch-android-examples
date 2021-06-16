package com.algolia.instantsearch.showcase.filter.facet.dynamic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.filter.facet.dynamic.DynamicFacetViewModel
import com.algolia.instantsearch.helper.filter.facet.dynamic.connectFilterState
import com.algolia.instantsearch.helper.filter.facet.dynamic.connectSearcher
import com.algolia.instantsearch.helper.filter.facet.dynamic.connectView
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.showcase.configureRecyclerView
import com.algolia.instantsearch.showcase.configureSearchView
import com.algolia.instantsearch.showcase.configureToolbar
import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import io.ktor.client.features.logging.*
import kotlinx.android.synthetic.main.include_search.*
import kotlinx.android.synthetic.main.showcase_facet_list_search.*

class DynamicFacetShowcase : AppCompatActivity() {

    private val client = ClientSearch(
            ConfigurationSearch(
                    ApplicationID("RVURKQXRHU"),
                    APIKey("937e4e6ec422ff69fe89b569dba30180"),
                    logLevel = LogLevel.ALL
            )
    )
    private val index = client.initIndex(IndexName("test_facet_ordering"))
    private val searcher = SearcherSingleIndex(index)
    private val filterState = FilterState()
    private val searchBox = SearchBoxConnector(searcher)

    private val color = Attribute("color")
    private val country = Attribute("country")
    private val brand = Attribute("brand")
    private val size = Attribute("size")

    private val dynamicFacetViewModel = DynamicFacetViewModel(
            selectionModeForAttribute = mapOf(
                    color to SelectionMode.Multiple,
                    country to SelectionMode.Multiple
            )
    )
    private val connection = ConnectionHandler(searchBox)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showcase_dynamic_facet_list)

        val searchBoxView = SearchBoxViewAppCompat(searchView)
        connection += searchBox.connectView(searchBoxView)

        val adapter = DynamicFacetAdapter()
        connection += dynamicFacetViewModel.connectSearcher(searcher)
        connection += dynamicFacetViewModel.connectFilterState(filterState)
        connection += dynamicFacetViewModel.connectView(adapter)

        configureToolbar(toolbar)
        configureSearchView(searchView, getString(R.string.search_brands))
        configureRecyclerView(hits, adapter)

        searcher.query.facets = setOf(brand, color, size, country)
        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}