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
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.helper.filter.facet.FacetListConnector
import com.algolia.instantsearch.helper.filter.facet.connectView
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.instantsearch.showcase.compose.*
import com.algolia.instantsearch.showcase.compose.filter.HeaderFilterConnector
import com.algolia.instantsearch.showcase.compose.ui.HoloBlueDark
import com.algolia.instantsearch.showcase.compose.ui.HoloGreenDark
import com.algolia.instantsearch.showcase.compose.ui.HoloRedDark
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.component.FilterList
import com.algolia.instantsearch.showcase.compose.ui.component.HeaderFilter
import com.algolia.instantsearch.showcase.compose.ui.component.SearchTopBar
import com.algolia.instantsearch.showcase.compose.ui.component.TitleTopBar
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName

class FacetListPersistentShowcase : AppCompatActivity() {

    private val color = Attribute("color")
    private val category = Attribute("category")
    private val index = client.initIndex(IndexName("stub"))
    private val filterState = FilterState()
    private val searcher = SearcherSingleIndex(index)

    private val facetListStateColor = FacetListState()
    private val facetListColor = FacetListConnector(
        searcher = searcher,
        filterState = filterState,
        attribute = color,
        selectionMode = SelectionMode.Multiple,
        persistentSelection = true
    )

    private val facetListStateCategory = FacetListState()
    private val facetListCategory = FacetListConnector(
        searcher = searcher,
        filterState = filterState,
        attribute = category,
        selectionMode = SelectionMode.Single,
        persistentSelection = true
    )

    private val filterHeader = HeaderFilterConnector(
        searcher = searcher,
        filterState = filterState,
        filterColors = filterColors(color, color, category)
    )

    private val connection = ConnectionHandler(
        facetListColor,
        facetListCategory,
        searcher.connectFilterState(filterState),
        facetListColor.connectView(facetListStateColor),
        facetListCategory.connectView(facetListStateCategory),
        filterHeader

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                FacetListPersistentScreen()
            }
        }

        configureSearcher(searcher)
        searcher.searchAsync()
    }

    @Composable
    fun FacetListPersistentScreen(title: String = showcaseTitle) {
        Scaffold(
            topBar = {
                TitleTopBar(
                    title = title,
                    onBackPressed = ::onBackPressed
                )
            },
            content = {
                Column(Modifier.fillMaxWidth()) {
                    HeaderFilter(
                        modifier = Modifier.padding(16.dp),
                        filterHeader = filterHeader
                    )
                    Row(Modifier.padding(horizontal = 16.dp)) {
                        FilterList(
                            modifier = Modifier.weight(0.5f),
                            titleColor = HoloRedDark,
                            title = "Multiple Choice",
                            facetListState = facetListStateColor
                        )
                        FilterList(
                            modifier = Modifier.weight(0.5f),
                            titleColor = HoloGreenDark,
                            title = "Since Choice",
                            facetListState = facetListStateCategory
                        )
                    }
                }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
