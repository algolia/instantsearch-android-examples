package com.algolia.instantsearch.showcase.filter.facet.dynamic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.helper.android.filter.facet.dynamic.DynamicFacetListAdapter
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.filter.facet.dynamic.DynamicFacetListConnector
import com.algolia.instantsearch.helper.filter.facet.dynamic.connectView
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.showcase.configureRecyclerView
import com.algolia.instantsearch.showcase.configureSearchView
import com.algolia.instantsearch.showcase.configureToolbar
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import io.ktor.client.features.logging.*
import kotlinx.android.synthetic.main.include_search.*
import kotlinx.android.synthetic.main.showcase_facet_list_search.*

class DynamicFacetShowcase : AppCompatActivity() {

    val client = ClientSearch(
        ApplicationID("RVURKQXRHU"),
        APIKey("937e4e6ec422ff69fe89b569dba30180"),
        LogLevel.ALL
    )
    val searcher = HitsSearcher(client, IndexName("test_facet_ordering"))
    val filterState = FilterState()
    val searchBox = SearchBoxConnector(searcher)
    val color = Attribute("color")
    val country = Attribute("country")
    val brand = Attribute("brand")
    val size = Attribute("size")
    val dynamicFacets = DynamicFacetListConnector(
        searcher = searcher,
        filterState = filterState,
        selectionModeForAttribute = mapOf(
            color to SelectionMode.Multiple,
            country to SelectionMode.Multiple
        ),
        filterGroupForAttribute = mapOf(
            brand to (brand to FilterOperator.Or),
            color to (color to FilterOperator.Or),
            size to (color to FilterOperator.Or),
            country to (color to FilterOperator.Or),
        )
    )
    private val connection = ConnectionHandler(searchBox, dynamicFacets)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showcase_dynamic_facet_list)

        val searchBoxView = SearchBoxViewAppCompat(searchView)
        connection += searchBox.connectView(searchBoxView)

        val factory = ViewHolderFactory()
        val adapter = DynamicFacetListAdapter(factory)
        connection += dynamicFacets.connectView(adapter)

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
