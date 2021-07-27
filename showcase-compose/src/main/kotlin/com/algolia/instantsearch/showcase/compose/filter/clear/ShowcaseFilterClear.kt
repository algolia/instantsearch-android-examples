package com.algolia.instantsearch.showcase.compose.filter.clear

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.filter.clear.FilterClear
import com.algolia.instantsearch.compose.item.StatsTextState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.filter.clear.ClearMode
import com.algolia.instantsearch.helper.filter.clear.FilterClearConnector
import com.algolia.instantsearch.helper.filter.clear.connectView
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.filters
import com.algolia.instantsearch.helper.filter.state.groupOr
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.instantsearch.helper.stats.StatsConnector
import com.algolia.instantsearch.helper.stats.StatsPresenterImpl
import com.algolia.instantsearch.helper.stats.connectView
import com.algolia.instantsearch.showcase.compose.configureSearcher
import com.algolia.instantsearch.showcase.compose.filter.FilterGroupsState
import com.algolia.instantsearch.showcase.compose.filter.current.ui.HeaderFilter
import com.algolia.instantsearch.showcase.compose.filter.current.ui.TitleTopBar
import com.algolia.instantsearch.showcase.compose.filterColors
import com.algolia.instantsearch.showcase.compose.showcaseTitle
import com.algolia.instantsearch.showcase.compose.stubIndex
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.component.RestoreFab
import com.algolia.search.model.Attribute

class ShowcaseFilterClear : AppCompatActivity() {

    private val searcher = SearcherSingleIndex(stubIndex)
    private val color = Attribute("color")
    private val category = Attribute("category")
    private val groupColor = groupOr(color)
    private val groupCategory = groupOr(category)
    private val filters = filters {
        group(groupColor) {
            facet(color, "red")
            facet(color, "green")
        }
        group(groupCategory) {
            facet(category, "shoe")
        }
    }
    private val filterState = FilterState(filters)
    private val filterGroupsState = FilterGroupsState(filterState)

    private val clearAll = FilterClear()
    private val filterClearAll = FilterClearConnector(filterState)

    private val clearSpecified = FilterClear()
    private val filterClearSpecified =
        FilterClearConnector(filterState, listOf(groupColor), ClearMode.Specified)

    private val clearExcept = FilterClear()
    private val filterClearExcept =
        FilterClearConnector(filterState, listOf(groupColor), ClearMode.Except)

    private val hitsStats = StatsTextState()
    private val stats = StatsConnector(searcher)

    private val colors = filterColors(color, category)

    private val connection = ConnectionHandler(
        filterClearAll,
        filterClearSpecified,
        filterClearExcept,
        stats,
        // connections
        searcher.connectFilterState(filterState),
        filterClearAll.connectView(clearAll),
        filterClearSpecified.connectView(clearSpecified),
        filterClearExcept.connectView(clearExcept),
        stats.connectView(hitsStats, StatsPresenterImpl()),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                FilterClearScreen()
            }
        }
        configureSearcher(searcher)
        searcher.searchAsync()
    }

    @Composable
    fun FilterClearScreen(title: String = showcaseTitle) {
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
                        filterGroups = filterGroupsState.filterGroups,
                        onClear = clearAll::clear,
                        stats = hitsStats.stats,
                        colors = colors
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = clearSpecified::clear,
                        ) {
                            Text(text = "CLEAR SPECIFIED")
                        }
                        Button(
                            onClick = clearExcept::clear,
                            colors = buttonColors(backgroundColor = MaterialTheme.colors.secondary)
                        ) {
                            Text(text = "CLEAR EXCEPT")
                        }
                    }
                }
            },
            floatingActionButton = {
                RestoreFab { filterState.notify { set(filters) } }
            }
        )
    }
}
