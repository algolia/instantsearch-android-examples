package com.algolia.instantsearch.guides.gettingstarted

import androidx.lifecycle.ViewModel
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.helper.android.filter.state.connectPaginator
import com.algolia.instantsearch.helper.android.list.Paginator
import com.algolia.instantsearch.helper.android.searchbox.connectPaginator
import com.algolia.instantsearch.helper.filter.facet.FacetListConnector
import com.algolia.instantsearch.helper.filter.facet.FacetListPresenterImpl
import com.algolia.instantsearch.helper.filter.facet.FacetSortCriterion
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.instantsearch.helper.stats.StatsConnector
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import io.ktor.client.features.logging.*
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class MyViewModel : ViewModel() {

    val client = ClientSearch(
        ApplicationID("latency"),
        APIKey("1f6fd3a6fb973cb08419fe7d288fa4db"),
        LogLevel.ALL
    )
    val searcher = HitsSearcher(client = client, indexName = IndexName("bestbuy_promo"))
    val paginator = Paginator(
        searcher = searcher,
        pagingConfig = PagingConfig(pageSize = 50, enablePlaceholders = false)
    ) { hit ->
        Product(
            hit.json.getValue("name").jsonPrimitive.content,
            hit.json["_highlightResult"]?.jsonObject
        )
    }
    val products = paginator.pager.liveData
    val searchBox = SearchBoxConnector(searcher)
    val stats = StatsConnector(searcher)

    val filterState = FilterState()
    val facetList = FacetListConnector(
        searcher = searcher,
        filterState = filterState,
        attribute = Attribute("category"),
        selectionMode = SelectionMode.Single
    )
    val facetPresenter = FacetListPresenterImpl(
        sortBy = listOf(FacetSortCriterion.CountDescending, FacetSortCriterion.IsRefined),
        limit = 100
    )
    val connection = ConnectionHandler()

    init {
        connection += searchBox
        connection += stats
        connection += facetList
        connection += searcher.connectFilterState(filterState)
        connection += filterState.connectPaginator(paginator)
        connection += searchBox.connectPaginator(paginator)
    }

    override fun onCleared() {
        super.onCleared()
        searcher.cancel()
        connection.clear()
    }
}
