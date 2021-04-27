package com.algolia.instantsearch.showcase.filter.facet.dynamic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.filter.facet.dynamic.DynamicFacetViewModel
import com.algolia.instantsearch.helper.filter.facet.dynamic.connectFilterState
import com.algolia.instantsearch.helper.filter.facet.dynamic.connectSearcher
import com.algolia.instantsearch.helper.filter.facet.dynamic.connectView
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.showcase.*
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import kotlinx.android.synthetic.main.include_search.*
import kotlinx.android.synthetic.main.showcase_facet_list_search.*

class DynamicFacetShowcase : AppCompatActivity() {

    private val index = client.initIndex(IndexName("stub"))
    private val searcher = SearcherSingleIndex(index)
    private val filterState = FilterState()
    private val searchBox = SearchBoxConnector(searcher)
    private val dynamicFacetViewModel = DynamicFacetViewModel(emptyList(), mutableMapOf())
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
        configureSearcher(searcher)
        configureSearchView(searchView, getString(R.string.search_brands))
        configureRecyclerView(hits, adapter)

        searcher.query.facets = setOf(Attribute("brand"), Attribute("color"), Attribute("size"), Attribute("country"))
        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}