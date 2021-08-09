package com.algolia.instantsearch.showcase.compose.list.paging

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
import androidx.compose.ui.unit.dp
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.algolia.instantsearch.compose.item.StatsTextState
import com.algolia.instantsearch.compose.list.Paginator
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.compose.searchbox.connectPaginator
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.stats.StatsConnector
import com.algolia.instantsearch.helper.stats.StatsPresenterImpl
import com.algolia.instantsearch.helper.stats.connectView
import com.algolia.instantsearch.showcase.compose.configureSearcher
import com.algolia.instantsearch.showcase.compose.model.Movie
import com.algolia.instantsearch.showcase.compose.stubIndex
import com.algolia.instantsearch.showcase.compose.ui.GreyLight
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.component.MoviesList
import com.algolia.instantsearch.showcase.compose.ui.component.SearchTopBar
import com.algolia.search.helper.deserialize
import kotlinx.coroutines.flow.Flow

class PagingSingleIndexShowcase : AppCompatActivity() {

    private val searcher = SearcherSingleIndex(stubIndex)
    private val pagingConfig = PagingConfig(pageSize = 10)
    private val paginator = Paginator(searcher, pagingConfig) {
        it.hits.deserialize(Movie.serializer())
    }
    private val searchBoxState = SearchBoxState()
    private val searchBox = SearchBoxConnector(searcher)

    private val statsState = StatsTextState()
    private val stats = StatsConnector(searcher)

    private val connection = ConnectionHandler(
        searchBox,
        stats,
        searchBox.connectView(searchBoxState),
        searchBox.connectPaginator(paginator),
        stats.connectView(statsState, StatsPresenterImpl())
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                PagingSingleIndexScreen(
                    stats = statsState.stats,
                    moviesFlow = paginator.flow
                )
            }
        }
        configureSearcher(searcher)
    }

    @Composable
    fun PagingSingleIndexScreen(stats: String, moviesFlow: Flow<PagingData<Movie>>) {
        Scaffold(
            topBar = {
                SearchTopBar(
                    placeHolderText = "Search for movies",
                    searchBoxState = searchBoxState,
                    onBackPressed = ::onBackPressed
                )
            },
            content = {
                Column(Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier.padding(12.dp),
                        text = stats,
                        style = MaterialTheme.typography.caption,
                        maxLines = 1,
                        color = GreyLight,
                    )
                    MoviesList(movies = moviesFlow.collectAsLazyPagingItems())
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
