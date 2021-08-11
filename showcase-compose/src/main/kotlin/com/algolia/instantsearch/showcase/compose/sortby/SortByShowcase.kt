package com.algolia.instantsearch.showcase.compose.sortby

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.compose.sortby.SortByState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.sortby.SortByConnector
import com.algolia.instantsearch.helper.sortby.connectView
import com.algolia.instantsearch.showcase.compose.client
import com.algolia.instantsearch.showcase.compose.model.Movie
import com.algolia.instantsearch.showcase.compose.showcaseTitle
import com.algolia.instantsearch.showcase.compose.ui.component.DropdownList
import com.algolia.instantsearch.showcase.compose.ui.component.TitleTopBar
import com.algolia.search.helper.deserialize
import com.algolia.search.model.IndexName

class SortByShowcase : AppCompatActivity() {

    private val indexTitle = client.initIndex(IndexName("mobile_demo_movies"))
    private val indexYearAsc = client.initIndex(IndexName("mobile_demo_movies_year_asc"))
    private val indexYearDesc = client.initIndex(IndexName("mobile_demo_movies_year_desc"))
    private val searcher = SearcherSingleIndex(indexTitle)
    private val hitsState = HitsState<Movie>()
    private val indexes = mapOf(
        0 to indexTitle,
        1 to indexYearAsc,
        2 to indexYearDesc
    )
    private val sortByState = SortByState()
    private val sortBy = SortByConnector(indexes, searcher, selected = 0)
    private val connection = ConnectionHandler(
        sortBy,
        searcher.connectHitsView(hitsState) { it.hits.deserialize(Movie.serializer()) },
        sortBy.connectView(sortByState) { index ->
            when (index) {
                indexTitle -> "Default"
                indexYearAsc -> "Year Asc"
                indexYearDesc -> "Year Desc"
                else -> index.indexName.raw
            }
        },
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SortByScreen()
        }
        searcher.searchAsync()
    }

    @Composable
    fun SortByScreen(title: String = showcaseTitle) {
        Scaffold(
            topBar = {
                TitleTopBar(
                    title = title,
                    onBackPressed = ::onBackPressed
                )
            },
            content = {
                DropdownList(
                    sortByState = sortByState
                )
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
