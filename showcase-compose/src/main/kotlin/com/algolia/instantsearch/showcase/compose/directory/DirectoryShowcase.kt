package com.algolia.instantsearch.showcase.compose.directory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.compose.searchbox.SearchBox
import com.algolia.instantsearch.compose.searchbox.SearchQuery
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.showcase.compose.R
import com.algolia.instantsearch.showcase.compose.client
import com.algolia.instantsearch.showcase.compose.configureSearchBox
import com.algolia.instantsearch.showcase.compose.ui.GreyLight
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.instantsearch.showcase.compose.ui.White
import com.algolia.search.helper.deserialize
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query

class DirectoryShowcase : ComponentActivity() {

    private val index = client.initIndex(IndexName("mobile_demo_home"))
    private val searcher = SearcherSingleIndex(index, Query(hitsPerPage = 100))
    private val hitsState = HitsState<DirectoryItem>(emptyList())
    private val searchQuery = SearchQuery()
    private val connections = ConnectionHandler()

    init {
        connections += searcher.connectHitsView(hitsState) { response ->
            response.hits.deserialize(DirectoryHit.serializer())
                .filter { showcases.containsKey(it.objectID) }
                .groupBy { it.type }
                .toSortedMap()
                .flatMap { (key, value) ->
                    listOf(DirectoryItem.Header(key)) + value.map { DirectoryItem.Item(it) }
                        .sortedBy { it.hit.objectID.raw }
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowcaseTheme {
                Column(Modifier.background(MaterialTheme.colors.background)) {
                    Surface(
                        elevation = 1.dp,
                        color = MaterialTheme.colors.surface,
                    ) {
                        SearchBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            searchQuery = searchQuery,
                            placeHolderText = getString(R.string.search_showcases)
                        )
                    }
                    LazyColumn {
                        items(hitsState.hits) { item ->
                            when (item) {
                                is DirectoryItem.Header -> Text(
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 6.dp
                                    ),
                                    text = item.name,
                                    style = MaterialTheme.typography.subtitle2,
                                    color = GreyLight,
                                    maxLines = 1
                                )
                                is DirectoryItem.Item -> Surface(
                                    elevation = 1.dp,
                                    color = MaterialTheme.colors.surface,
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(White)
                                            .padding(horizontal = 12.dp, vertical = 12.dp),
                                        text = item.hit.name,
                                        style = MaterialTheme.typography.body1,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        configureSearchBox(searcher, searchQuery, connections)
        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connections.clear()
    }
}
