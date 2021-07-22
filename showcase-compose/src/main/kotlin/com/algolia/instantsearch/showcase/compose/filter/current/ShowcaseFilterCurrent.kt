package com.algolia.instantsearch.showcase.compose.filter.current

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.algolia.instantsearch.compose.filter.current.FilterCurrentState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.filter.current.FilterCurrentConnector
import com.algolia.instantsearch.helper.filter.current.connectView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.filters
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.instantsearch.showcase.compose.R
import com.algolia.instantsearch.showcase.compose.configureSearcher
import com.algolia.instantsearch.showcase.compose.stubIndex
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.NumericOperator
import com.algolia.search.serialize.KeyName


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
    private val currentFiltersColor = FilterCurrentConnector(filterState, listOf(groupColor))
    private val currentFiltersAll = FilterCurrentConnector(filterState)

    private val chipGroupAll = FilterCurrentState()
    private val chipGroupColors = FilterCurrentState()


    private val connection = ConnectionHandler(
        currentFiltersColor,
        currentFiltersAll,
        searcher.connectFilterState(filterState),
        currentFiltersAll.connectView(chipGroupAll),
        currentFiltersAll.connectView(chipGroupColors),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val title = intent.extras?.getString(KeyName) ?: ""
        setContent {
            Column(Modifier.fillMaxWidth()) {
                TitleTopBar(title = title)
                HeaderFilter(filterCurrentState = chipGroupAll)
            }
        }


        configureSearcher(searcher)

        //configureToolbar(toolbar)
        //onFilterChangedThenUpdateFiltersText(filterState, filtersTextView, color, price, tags)
        //onErrorThenUpdateFiltersText(searcher, filtersTextView)
        //onResponseChangedThenUpdateNbHits(searcher, nbHits, connection)
        //onClearAllThenClearFilters(filterState, filtersClearAll, connection)
        //onResetThenRestoreFilters(reset, filterState, filters)

        searcher.searchAsync()
    }

    //fun AppCompatActivity.configureToolbar(toolbar: Toolbar) {
    //    setSupportActionBar(toolbar)
    //    supportActionBar?.let {
    //        it.title = intent.extras?.getString(KeyName)
    //        it.setDisplayHomeAsUpEnabled(true)
    //    }
    //}

    @Composable
    fun TitleTopBar(modifier: Modifier = Modifier, title: String) {
        TopAppBar(title = { Text(title) })
    }

    @Composable
    fun HeaderFilter(
        modifier: Modifier = Modifier,
        title: String = stringResource(R.string.filters),
        onClear: () -> Unit = {},
        filterCurrentState: FilterCurrentState,
    ) {
        Column(modifier) {
            Row(Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = title
                )
                Icon(
                    modifier = Modifier.clickable { onClear() },
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}