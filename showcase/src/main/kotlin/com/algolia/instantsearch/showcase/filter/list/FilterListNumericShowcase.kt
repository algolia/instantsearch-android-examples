package com.algolia.instantsearch.showcase.filter.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.filter.list.FilterListConnector
import com.algolia.instantsearch.helper.filter.list.connectView
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.groupAnd
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.*
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.NumericOperator
import kotlinx.android.synthetic.main.header_filter.*
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.showcase_filter_list.*


class FilterListNumericShowcase : AppCompatActivity() {

    private val price = Attribute("price")
    private val groupPrice = groupAnd(price)
    private val searcher = HitsSearcher(client, stubIndexName)
    private val filterState = FilterState()
    private val filters = listOf(
        Filter.Numeric(price, NumericOperator.Less, 5),
        Filter.Numeric(price, 5..10),
        Filter.Numeric(price, 10..25),
        Filter.Numeric(price, 25..100),
        Filter.Numeric(price, NumericOperator.Greater, 100)
    )
    private val filterList = FilterListConnector.Numeric(filters, filterState, groupID = groupPrice)
    private val connection = ConnectionHandler(filterList, searcher.connectFilterState(filterState))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showcase_filter_list)

        val viewNumeric = FilterListAdapter<Filter.Numeric>()

        connection += searcher.connectFilterState(filterState)
        connection += filterList.connectView(viewNumeric)

        configureToolbar(toolbar)
        configureSearcher(searcher)
        configureRecyclerView(listTopLeft, viewNumeric)
        onFilterChangedThenUpdateFiltersText(filterState, filtersTextView, price)
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
}