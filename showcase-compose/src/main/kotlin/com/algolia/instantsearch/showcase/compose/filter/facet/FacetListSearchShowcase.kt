package com.algolia.instantsearch.showcase.compose.filter.facet

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.filter.facet.FacetListState
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.helper.filter.facet.FacetListConnector
import com.algolia.instantsearch.helper.filter.facet.FacetListPresenterImpl
import com.algolia.instantsearch.helper.filter.facet.FacetSortCriterion
import com.algolia.instantsearch.helper.filter.facet.connectView
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.instantsearch.showcase.compose.*
import com.algolia.instantsearch.showcase.compose.ui.component.HeaderFilterConnector
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.component.FacetList
import com.algolia.instantsearch.showcase.compose.ui.component.HeaderFilter
import com.algolia.instantsearch.showcase.compose.ui.component.SearchTopBar
import com.algolia.search.model.Attribute

class FacetListSearchShowcase : AppCompatActivity() {

    private val brand = Attribute("brand")
    private val searcher = SearcherSingleIndex(stubIndex)
    private val searcherForFacet = SearcherForFacets(stubIndex, brand)
    private val filterState = FilterState()

    private val searchBoxState = SearchBoxState()
    private val searchBox = SearchBoxConnector(searcherForFacet)

    private val facetListState = FacetListState()
    private val facetPresenter = FacetListPresenterImpl(
        sortBy = listOf(FacetSortCriterion.IsRefined, FacetSortCriterion.CountDescending),
        limit = 100
    )
    private val facetList = FacetListConnector(
        searcher = searcherForFacet,
        filterState = filterState,
        attribute = brand,
        selectionMode = SelectionMode.Multiple
    )
    private val filterHeader = HeaderFilterConnector(
        searcher = searcher,
        filterState = filterState,
        filterColors = filterColors(brand)
    )

    private val connection = ConnectionHandler(
        searchBox,
        facetList,
        searcher.connectFilterState(filterState),
        searchBox.connectView(searchBoxState),
        facetList.connectView(facetListState, facetPresenter),
        filterHeader
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                FilterScreenScreen()
            }
        }

        val index = client.initIndex(intent.indexName)
        searcher.index = index
        searcherForFacet.index = index
        searcher.searchAsync()
        searcherForFacet.searchAsync()
    }

    @Composable
    fun FilterScreenScreen() {
        Scaffold(
            topBar = {
                SearchTopBar(
                    searchBoxState = searchBoxState,
                    onBackPressed = ::onBackPressed
                )
            },
            content = {
                Column(Modifier.fillMaxWidth()) {
                    HeaderFilter(
                        modifier = Modifier.padding(16.dp),
                        filterHeader = filterHeader
                    )
                    val scrollState = rememberScrollState()
                    FacetList(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(scrollState),
                        facetListState = facetListState
                    )
                }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        searcherForFacet.cancel()
        connection.clear()
    }
}
