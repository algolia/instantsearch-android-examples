package com.algolia.instantsearch.showcase.compose.filter.current

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.filter.clear.FilterClear
import com.algolia.instantsearch.compose.filter.current.FilterCurrentState
import com.algolia.instantsearch.compose.item.StatsTextState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.filter.clear.FilterClearConnector
import com.algolia.instantsearch.helper.filter.clear.connectView
import com.algolia.instantsearch.helper.filter.current.FilterCurrentConnector
import com.algolia.instantsearch.helper.filter.current.connectView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.filters
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.instantsearch.helper.stats.StatsConnector
import com.algolia.instantsearch.helper.stats.StatsPresenterImpl
import com.algolia.instantsearch.helper.stats.connectView
import com.algolia.instantsearch.showcase.compose.configureSearcher
import com.algolia.instantsearch.showcase.compose.filter.FilterGroupsState
import com.algolia.instantsearch.showcase.compose.filter.current.ui.FilterChips
import com.algolia.instantsearch.showcase.compose.filter.current.ui.HeaderFilter
import com.algolia.instantsearch.showcase.compose.filter.current.ui.TitleTopBar
import com.algolia.instantsearch.showcase.compose.filterColors
import com.algolia.instantsearch.showcase.compose.showcaseTitle
import com.algolia.instantsearch.showcase.compose.stubIndex
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.component.RestoreFab
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.NumericOperator

class ShowcaseFilterCurrent : AppCompatActivity() {

    private val color = Attribute("color")
    private val price = Attribute("price")
    private val tags = Attribute("_tags")
    private val groupColor = FilterGroupID(color)
    private val groupPrice = FilterGroupID(price)
    private val groupTags = FilterGroupID(tags)
    private val filters = filters {
        group(groupColor) {
            facet(color, "red")
            facet(color, "green")
        }
        group(groupTags) {
            tag("mobile")
        }
        group(groupPrice) {
            comparison(price, NumericOperator.NotEquals, 42)
            range(price, IntRange(0, 100))
        }
    }
    private val filterState = FilterState(filters)
    private val searcher = SearcherSingleIndex(stubIndex)
    private val currentFiltersAll = FilterCurrentConnector(filterState)
    private val currentFiltersColor = FilterCurrentConnector(filterState, listOf(groupColor))

    private val hitsStats = StatsTextState()
    private val stats = StatsConnector(searcher)

    private val clearAll = FilterClear()
    private val filterClear = FilterClearConnector(filterState)

    private val chipGroupAll = FilterCurrentState()
    private val chipGroupColors = FilterCurrentState()

    private val connection = ConnectionHandler(
        // connectors
        currentFiltersAll,
        currentFiltersColor,
        filterClear,
        stats,
        // view connections
        searcher.connectFilterState(filterState),
        filterClear.connectView(clearAll),
        stats.connectView(hitsStats, StatsPresenterImpl()),
        currentFiltersAll.connectView(chipGroupAll),
        currentFiltersColor.connectView(chipGroupColors),
    )

    private val colors: Map<String, Color> = filterColors(color, price, tags)
    private val filterGroupsState = FilterGroupsState(filterState)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                FilterCurrentScreen()
            }
        }

        configureSearcher(searcher)
        searcher.searchAsync()
    }

    @Composable
    fun FilterCurrentScreen(title: String = showcaseTitle) {
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
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = "Current filters",
                        style = MaterialTheme.typography.caption
                    )
                    FilterChips(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        filterCurrentState = chipGroupAll,
                        rows = 2
                    )
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = "Current colors filters",
                        style = MaterialTheme.typography.caption
                    )
                    FilterChips(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        filterCurrentState = chipGroupColors,
                        rows = 1
                    )
                }
            },
            floatingActionButton = {
                RestoreFab { filterState.notify { set(filters) } }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
