package com.algolia.instantsearch.guides.gettingstarted

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.helper.android.filter.state.connectPagedList
import com.algolia.instantsearch.helper.android.list.SearcherSingleIndexDataSource
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxConnectorPagedList
import com.algolia.instantsearch.helper.filter.facet.FacetListConnector
import com.algolia.instantsearch.helper.filter.facet.FacetListPresenterImpl
import com.algolia.instantsearch.helper.filter.facet.FacetSortCriterion
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.instantsearch.helper.stats.StatsConnector
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import io.ktor.client.features.logging.LogLevel
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class MyViewModel : ViewModel() {

    val client = ClientSearch(
        ApplicationID("LNNFEEWZVA"),
        APIKey("200a3252844dc679d946e6a60ec60b93"),
        LogLevel.ALL
    )
    val index = client.initIndex(IndexName("prod_cex_uk"))
    val searcher = SearcherSingleIndex(index)

    val dataSourceFactory = SearcherSingleIndexDataSource.Factory(searcher) { hit ->
        Product(
            hit.json.getValue("boxName").jsonPrimitive.content,
            hit.json["_highlightResult"]?.jsonObject
        )
    }
    val pagedListConfig =
        PagedList.Config.Builder().setPageSize(50).setEnablePlaceholders(false).build()
    val products: LiveData<PagedList<Product>> =
        LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()
    val searchBox = SearchBoxConnectorPagedList(searcher, listOf(products))
    val stats = StatsConnector(searcher)

    val filterState = FilterState()
    val facetList = FacetListConnector(
        searcher = searcher,
        filterState = filterState,
        attribute = Attribute("stores"),
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
        connection += filterState.connectPagedList(products)
    }

    override fun onCleared() {
        super.onCleared()
        searcher.cancel()
        connection.clear()
    }
}