package com.algolia.instantsearch.showcase.filter.rating

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.helper.filter.range.FilterRangeConnector
import com.algolia.instantsearch.helper.filter.range.connectView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.filters
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.showcase.client
import com.algolia.instantsearch.showcase.configureToolbar
import com.algolia.instantsearch.showcase.onClearAllThenClearFilters
import com.algolia.instantsearch.showcase.onErrorThenUpdateFiltersText
import com.algolia.instantsearch.showcase.onFilterChangedThenUpdateFiltersText
import com.algolia.instantsearch.showcase.onResponseChangedThenUpdateNbHits
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import kotlinx.android.synthetic.main.header_filter.*
import kotlinx.android.synthetic.main.include_plus_minus.*
import kotlinx.android.synthetic.main.showcase_filter_rating.*

class RatingShowcase : AppCompatActivity() {

    private val index = client.initIndex(IndexName("instant_search"))
    private val searcher = SearcherSingleIndex(index)
    private val rating = Attribute("rating")
    private val groupID = FilterGroupID(rating)
    private val primaryBounds = 0f..5f
    private val initialRange = 3f..5f
    private val filters = filters {
        group(groupID) {
            +Filter.Numeric(
                rating,
                lowerBound = initialRange.start,
                upperBound = initialRange.endInclusive
            )
        }
    }
    private val filterState = FilterState(filters)
    private val range =
        FilterRangeConnector(filterState, rating, range = initialRange, bounds = primaryBounds)
    private val connection = ConnectionHandler(
        range,
        searcher.connectFilterState(filterState, Debouncer(100))
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showcase_filter_rating)

        val ratingBarView = RatingBarView(ratingBar).apply {
            stepSize = STEP
            plus.setOnClickListener { rating += stepSize }
            minus.setOnClickListener { rating -= stepSize }
        }
        connection += range.connectView(ratingBarView)
        connection += range.connectView(RatingTextView(ratingLabel))

        configureToolbar(toolbar)
        onFilterChangedThenUpdateFiltersText(filterState, filtersTextView, rating)
        onClearAllThenClearFilters(filterState, filtersClearAll, connection)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, nbHits, connection)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }

    companion object {
        private const val STEP = 0.1f
    }
}