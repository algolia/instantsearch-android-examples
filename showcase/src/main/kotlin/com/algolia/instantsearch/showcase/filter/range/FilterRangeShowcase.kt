package com.algolia.instantsearch.showcase.filter.range

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.filter.range.FilterRangeConnector
import com.algolia.instantsearch.helper.filter.range.connectSearcher
import com.algolia.instantsearch.helper.filter.range.connectView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.filters
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.SearchMode
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.instantsearch.showcase.*
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Query
import kotlinx.android.synthetic.main.header_filter.*
import kotlinx.android.synthetic.main.include_search.*
import kotlinx.android.synthetic.main.showcase_filter_range.*


class FilterRangeShowcase : AppCompatActivity() {

    private val price = Attribute("price")
    // To get facets_stats, we need facets to be set: https://www.algolia.com/doc/api-reference/api-methods/search/#method-response-facets_stats
    private val searcher = SearcherSingleIndex(stubIndex, Query(facets = setOf(price)))
    private val groupID = FilterGroupID(price)
    private val primaryBounds = 0..15
    private val secondaryBounds = 0..10
    private val initialRange = 0..15
    private val filters = filters {
        group(groupID) {
            range(price, initialRange)
        }
    }
    private val filterState = FilterState(filters)
    private val range =
        FilterRangeConnector(filterState, price, range = initialRange, bounds = primaryBounds)
    private val searchBox = SearchBoxConnector(searcher, searchMode = SearchMode.AsYouType)

    private val connection = ConnectionHandler(
        range,
        searcher.connectFilterState(filterState, Debouncer(100)),
        // Connect FilterRange to the Searcher.
        range.connectSearcher(searcher, price),
        searchBox
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showcase_filter_range)

        connection += range.connectView(RangeSliderView(slider))
        connection += range.connectView(RangeTextView(rangeLabel))
        connection += range.connectView(BoundsTextView(boundsLabel))
        val searchBoxView = SearchBoxViewAppCompat(searchView)
        connection += searchBox.connectView(searchBoxView)

            buttonChangeBounds.setOnClickListener {
                range.viewModel.bounds.value = Range(secondaryBounds)
                it.isEnabled = false
                buttonResetBounds.isEnabled = true
            }
        buttonResetBounds.setOnClickListener {
            range.viewModel.bounds.value = Range(primaryBounds)
            it.isEnabled = false
            buttonChangeBounds.isEnabled = true
        }

        reset.setOnClickListener {
            filterState.notify { set(filters) }
        }
        configureToolbar(toolbar)
        configureSearcher(searcher)
        onFilterChangedThenUpdateFiltersText(filterState, filtersTextView, price)
        onClearAllThenClearFilters(filterState, filtersClearAll, connection)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, nbHits, connection)
        configureSearchView(searchView, getString(R.string.search_items))

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}

