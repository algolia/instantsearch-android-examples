package com.algolia.instantsearch.showcase.compose.list.paging

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.algolia.instantsearch.compose.list.Paginator
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.compose.searchbox.connectPaginator
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.showcase.compose.client
import com.algolia.instantsearch.showcase.compose.model.Actor
import com.algolia.instantsearch.showcase.compose.model.Movie
import com.algolia.instantsearch.showcase.compose.ui.GreyLight
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.component.ActorsHorizontalList
import com.algolia.instantsearch.showcase.compose.ui.component.MoviesHorizontalList
import com.algolia.instantsearch.showcase.compose.ui.component.SearchTopBar
import com.algolia.search.model.IndexName
import com.algolia.search.model.multipleindex.IndexQuery
import kotlinx.coroutines.flow.Flow


class PagingMultipleIndexShowcase : AppCompatActivity() {

    private val indexMovies = IndexQuery(IndexName("mobile_demo_movies"))
    private val indexActors = IndexQuery(IndexName("mobile_demo_actors"))
    private val searcher = SearcherMultipleIndex(client, listOf(indexMovies, indexActors))
    private val moviesPaginator = Paginator(searcher, indexMovies) {
        it.deserialize(Movie.serializer())
    }
    private val actorsPaginator = Paginator(searcher, indexActors) {
        it.deserialize(Actor.serializer())
    }

    private val searchBoxState = SearchBoxState()
    private val searchBox = SearchBoxConnector(searcher)

    private val connections = ConnectionHandler(searchBox)

    init {
        connections += searchBox.connectView(searchBoxState)
        connections += searchBox.connectPaginator(moviesPaginator)
        connections += searchBox.connectPaginator(actorsPaginator)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setContent {
                ShowcaseTheme {
                    PagingMultipleIndexScreen(
                        moviesFlow = moviesPaginator.flow,
                        actorsFlow = actorsPaginator.flow
                    )
                }
            }
        }
    }

    @Composable
    fun PagingMultipleIndexScreen(
        moviesFlow: Flow<PagingData<Movie>>,
        actorsFlow: Flow<PagingData<Actor>>,
    ) {
        val moviesListState = rememberLazyListState()
        val actorsListState = rememberLazyListState()
        Scaffold(
            topBar = {
                SearchTopBar(
                    placeHolderText = "Search for movies",
                    searchBoxState = searchBoxState,
                    onBackPressed = ::onBackPressed,
                    lazyListStates = listOf(moviesListState, actorsListState)
                )
            },
            content = {
                Column(Modifier.fillMaxWidth()) {
                    Text(
                        text = "Movies", style = MaterialTheme.typography.subtitle2,
                        color = GreyLight,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    MoviesHorizontalList(
                        movies = moviesFlow.collectAsLazyPagingItems(),
                        listState = moviesListState
                    )
                    Text(
                        text = "Actors",
                        style = MaterialTheme.typography.subtitle2,
                        color = GreyLight,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    ActorsHorizontalList(
                        actors = actorsFlow.collectAsLazyPagingItems(),
                        listState = actorsListState
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
