package com.algolia.instantsearch.showcase.compose.list.merged

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.searcher.connectView
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.showcase.compose.client
import com.algolia.instantsearch.showcase.compose.model.Actor
import com.algolia.instantsearch.showcase.compose.model.Movie
import com.algolia.instantsearch.showcase.compose.ui.GreyLight
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.component.MoviesList
import com.algolia.instantsearch.showcase.compose.ui.component.SearchTopBar
import com.algolia.search.helper.deserialize
import com.algolia.search.model.IndexName
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.search.Query

class MergedListShowcase : AppCompatActivity() {

    private val searcher = SearcherMultipleIndex(
        client,
        listOf(
            IndexQuery(IndexName("mobile_demo_movies"), Query(hitsPerPage = 3)),
            IndexQuery(IndexName("mobile_demo_actors"), Query(hitsPerPage = 5))
        )
    )

    private val searchBoxState = SearchBoxState()
    private val searchBox = SearchBoxConnector(searcher)
    private var mergedList by mutableStateOf<MergedList?>(null)

    private val connection = ConnectionHandler(
        searchBox,
        searchBox.connectView(searchBoxState),
        searcher.connectView(view = { mergedList = it }) {
            it?.let { response ->
                val actors = response.results[1].hits.deserialize(Actor.serializer())
                val movies = response.results[0].hits.deserialize(Movie.serializer())
                MergedList(actors, movies)
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                MergedListScreen(mergedList)
            }
        }
        searcher.searchAsync()
    }

    @Composable
    fun MergedListScreen(mergedList: MergedList?) {
        Scaffold(
            topBar = {
                SearchTopBar(
                    placeHolderText = "Search for movies",
                    searchBoxState = searchBoxState,
                    onBackPressed = ::onBackPressed
                )
            },
            content = {
                val (actors, movies) = mergedList ?: return@Scaffold
                Column(Modifier.fillMaxWidth()) {
                    Text(
                        text = "Actors",
                        style = MaterialTheme.typography.subtitle2,
                        color = GreyLight,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    ActorsList(actors = actors)
                    Text(
                        text = "Movies", style = MaterialTheme.typography.subtitle2,
                        color = GreyLight,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    MoviesList(movies = movies)
                }
            }
        )
    }

    @Composable
    fun ActorsList(modifier: Modifier = Modifier, actors: List<Actor>) {
        LazyColumn(modifier) {
            items(actors) { actor ->
                Surface(elevation = 1.dp) {
                    ActorItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        actor = actor
                    )
                }
            }
        }
    }

    @Composable
    fun ActorItem(modifier: Modifier = Modifier, actor: Actor) {
        Text(modifier = modifier, text = actor.name)
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}

data class MergedList(val actors: List<Actor>, val movies: List<Movie>)
