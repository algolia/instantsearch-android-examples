package com.algolia.instantsearch.showcase.compose.loading

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.algolia.instantsearch.compose.list.Paginator
import com.algolia.instantsearch.compose.loading.LoadingState
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.compose.searchbox.connectPaginator
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.loading.LoadingConnector
import com.algolia.instantsearch.helper.loading.connectView
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.showcase.compose.configureSearcher
import com.algolia.instantsearch.showcase.compose.model.Movie
import com.algolia.instantsearch.showcase.compose.stubIndex
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.component.MoviesList
import com.algolia.instantsearch.showcase.compose.ui.component.SearchTopBar
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.Flow

class LoadingShowcase : AppCompatActivity() {

    private val searcher = SearcherSingleIndex(stubIndex)
    private val paginator = Paginator(searcher) { it.deserialize(Movie.serializer()) }

    private val loadingState = LoadingState()
    private val loading = LoadingConnector(searcher)

    private val searchBoxState = SearchBoxState()
    private val searchBox = SearchBoxConnector(searcher)

    private val connections = ConnectionHandler(loading, searchBox)

    init {
        connections += loading.connectView(loadingState)
        connections += searchBox.connectView(searchBoxState)
        connections += searchBox.connectPaginator(paginator)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                LoadingScreen(
                    loadingState = loadingState,
                    searchBoxState = searchBoxState,
                    moviesFlow = paginator.flow
                )
            }
        }
        configureSearcher(searcher)
        searcher.searchAsync()
    }

    @Composable
    fun LoadingScreen(
        loadingState: LoadingState,
        searchBoxState: SearchBoxState,
        moviesFlow: Flow<PagingData<Movie>>
    ) {
        val moviesListState = rememberLazyListState()
        Scaffold(
            topBar = {
                SearchTopBar(
                    placeHolderText = "Search for movies",
                    searchBoxState = searchBoxState,
                    onBackPressed = ::onBackPressed,
                    lazyListState = moviesListState
                )
            },
            content = {
                SwipeRefresh(
                    state = rememberSwipeRefreshState(loadingState.loading),
                    onRefresh = { loadingState.reload() },
                ) {
                    MoviesList(
                        movies = moviesFlow.collectAsLazyPagingItems(),
                        listState = moviesListState
                    )
                }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connections.clear()
    }
}
