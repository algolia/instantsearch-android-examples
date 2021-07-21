package com.algolia.searchapp

import androidx.lifecycle.ViewModel
import com.algolia.instantsearch.compose.filter.facet.FacetListState
import com.algolia.instantsearch.compose.filter.facet.connectPaginator
import com.algolia.instantsearch.compose.item.StatsTextState
import com.algolia.instantsearch.compose.list.Paginator
import com.algolia.instantsearch.compose.searchbox.SearchQuery
import com.algolia.instantsearch.compose.searchbox.connectPaginator
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.helper.filter.facet.FacetListConnector
import com.algolia.instantsearch.helper.filter.facet.connectView
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.instantsearch.helper.stats.StatsConnector
import com.algolia.instantsearch.helper.stats.StatsPresenterImpl
import com.algolia.instantsearch.helper.stats.connectView
import com.algolia.search.client.ClientSearch
import com.algolia.search.helper.deserialize
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import io.ktor.client.features.logging.*

class MainViewModel : ViewModel() {

    val client = ClientSearch(
        ApplicationID("latency"),
        APIKey("1f6fd3a6fb973cb08419fe7d288fa4db"),
        LogLevel.ALL
    )
    val index = client.initIndex(IndexName("bestbuy"))
    val searcher = SearcherSingleIndex(index)

    // Search Box
    val searchQuery = SearchQuery()
    val searchBoxConnector = SearchBoxConnector(searcher)

    // Hits
    val hitsPaginator = Paginator(searcher) { response ->
        response.hits.deserialize(Product.serializer())
    }

    // Stats
    val statsText = StatsTextState()
    val statsConnector = StatsConnector(searcher)

    // Filters
    val facetList = FacetListState()
    val filterState = FilterState()
    val manufacturer = Attribute("category")
    val searcherForFacet = SearcherForFacets(index, manufacturer)
    val facetListConnector = FacetListConnector(
        searcher = searcherForFacet,
        filterState = filterState,
        attribute = manufacturer,
        selectionMode = SelectionMode.Multiple
    )

    val connections = ConnectionHandler(searchBoxConnector, statsConnector, facetListConnector)

    init {
        connections += searchBoxConnector.connectView(searchQuery)
        connections += statsConnector.connectView(statsText, StatsPresenterImpl())
        connections += searcher.connectFilterState(filterState)
        connections += facetListConnector.connectView(facetList)
        connections += facetListConnector.connectPaginator(hitsPaginator)
        connections += searchBoxConnector.connectPaginator(hitsPaginator)

        searcherForFacet.searchAsync()
    }

    override fun onCleared() {
        super.onCleared()
        searcher.cancel()
        connections.clear()
        searcherForFacet.cancel()
    }
}
