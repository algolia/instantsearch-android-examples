package com.algolia.instantsearch.showcase.compose.filter.clear

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.filter.clear.FilterClear
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.filter.clear.ClearMode
import com.algolia.instantsearch.helper.filter.clear.FilterClearConnector
import com.algolia.instantsearch.helper.filter.clear.connectView
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.filters
import com.algolia.instantsearch.helper.filter.state.groupOr
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.instantsearch.showcase.compose.configureSearcher
import com.algolia.instantsearch.showcase.compose.filter.HeaderFilterConnector
import com.algolia.instantsearch.showcase.compose.filter.current.ui.HeaderFilter
import com.algolia.instantsearch.showcase.compose.filter.current.ui.TitleTopBar
import com.algolia.instantsearch.showcase.compose.filterColors
import com.algolia.instantsearch.showcase.compose.showcaseTitle
import com.algolia.instantsearch.showcase.compose.stubIndex
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.component.RestoreFab
import com.algolia.search.model.Attribute

class FilterClearShowcase : AppCompatActivity() {

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
    private val clearSpecified = FilterClear()
    private val filterClearSpecified =
        FilterClearConnector(filterState, listOf(groupColor), ClearMode.Specified)
    private val clearExcept = FilterClear()
    private val filterClearExcept =
        FilterClearConnector(filterState, listOf(groupColor), ClearMode.Except)

    private val filterHeader = HeaderFilterConnector(
        searcher = searcher,
        filterState = filterState,
        filterColors = filterColors(color, category)
    )

    private val connection = ConnectionHandler(
        filterClearSpecified,
        filterClearExcept,
        searcher.connectFilterState(filterState),
        filterClearSpecified.connectView(clearSpecified),
        filterClearExcept.connectView(clearExcept),
        filterHeader
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
                        filterHeader = filterHeader,
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

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
