package com.algolia.instantsearch.showcase.compose.highlighting

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.algolia.instantsearch.compose.highlighting.toAnnotatedString
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.compose.searchbox.SearchQuery
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.showcase.compose.configureSearcher
import com.algolia.instantsearch.showcase.compose.model.Movie
import com.algolia.instantsearch.showcase.compose.stubIndex
import com.algolia.instantsearch.showcase.compose.ui.GreyDark
import com.algolia.instantsearch.showcase.compose.ui.White
import com.algolia.instantsearch.showcase.compose.ui.component.SearchTopBar
import com.algolia.search.helper.deserialize
import java.util.*

class HighlightingShowcase : AppCompatActivity() {

    private val searcher = SearcherSingleIndex(stubIndex)
    private val hitsState = HitsState<Movie>()

    private val searchQuery = SearchQuery()
    private val searchBox = SearchBoxConnector(searcher)

    private val connection = ConnectionHandler(
        searchBox,
        searcher.connectHitsView(hitsState) { it.hits.deserialize(Movie.serializer()) },
        searchBox.connectView(searchQuery)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HighlightingShowcase()
        }
        configureSearcher(searcher)
        searcher.searchAsync()
    }

    @Composable
    fun HighlightingShowcase() {
        Scaffold(
            topBar = {
                SearchTopBar(
                    placeHolderText = "Search for movies",
                    searchQuery = searchQuery,
                    onBackPressed = ::onBackPressed
                )
            },
            content = {
                MoviesList(
                    modifier = Modifier.background(White),
                    movies = hitsState.hits
                )
            }
        )
    }

    @Composable
    fun MoviesList(modifier: Modifier = Modifier, movies: List<Movie>) {
        LazyColumn(modifier) {
            items(movies) { movie ->
                Surface(elevation = 1.dp) {
                    MovieItem(
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth()
                            .padding(16.dp),
                        movie = movie
                    )
                }
            }
        }
    }

    @Composable
    fun MovieItem(modifier: Modifier = Modifier, movie: Movie) {
        Row(modifier, verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.size(68.dp),
                painter = rememberImagePainter(
                    data = movie.image,
                    builder = {
                        placeholder(android.R.drawable.ic_media_play)
                        error(android.R.drawable.ic_media_play)
                    },
                ),
                contentDescription = "movie image",
            )

            Column(
                Modifier
                    .padding(start = 16.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = titleOf(movie),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = genresOf(movie),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body2,
                    color = GreyDark
                )
                Text(
                    text = actorsOf(movie),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.caption,
                    color = GreyDark
                )
            }
        }
    }

    private fun titleOf(movie: Movie): AnnotatedString {
        return movie.highlightedTitle?.toAnnotatedString()
            ?: AnnotatedString(movie.title) + AnnotatedString(" ($movie.year)")
    }

    private fun genresOf(movie: Movie): AnnotatedString {
        return movie.highlightedGenres?.toAnnotatedString(SpanStyle(background = Color.Yellow))
            ?: AnnotatedString("unknown genre", SpanStyle(fontStyle = FontStyle.Italic))
    }

    private fun actorsOf(movie: Movie): String {
        return movie.highlightedActors?.let { list ->
            list.sortedByDescending { it.highlightedTokens.size }
                .joinToString { highlight ->
                    highlight.tokens.joinToString("") {
                        if (it.highlighted) it.content.uppercase(Locale.getDefault()) else it.content
                    }
                }
        } ?: ""
    }

    @Composable
    fun TextAnnotated(
        text: CharSequence,
        modifier: Modifier = Modifier,
        style: TextStyle = LocalTextStyle.current
    ) {
        when (text) {
            is AnnotatedString -> Text(modifier = modifier, text = text, style = style)
            else -> Text(modifier = modifier, text = text.toString(), style = style)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
