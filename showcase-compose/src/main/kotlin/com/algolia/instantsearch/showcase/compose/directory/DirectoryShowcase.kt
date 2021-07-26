package com.algolia.instantsearch.showcase.compose.directory

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.compose.searchbox.SearchQuery
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.showcase.compose.client
import com.algolia.instantsearch.showcase.compose.configureSearchBox
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme
import com.algolia.search.helper.deserialize
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query
import com.algolia.search.serialize.KeyIndexName
import com.algolia.search.serialize.KeyName

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
                Directory(searchQuery, hitsState, ::navigateToShowcase)
            }
        }
        configureSearchBox(searcher, searchQuery, connections)
        searcher.searchAsync()
    }

    private fun navigateToShowcase(item: DirectoryItem.Item) {
        val intent = Intent(this, showcases.getValue(item.hit.objectID).java).apply {
            putExtra(KeyIndexName, item.hit.index)
            putExtra(KeyName, item.hit.name)
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connections.clear()
    }
}
