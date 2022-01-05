package com.algolia.instantsearch.showcase.directory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.*
import com.algolia.search.helper.deserialize
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query
import kotlinx.android.synthetic.main.include_search.*
import kotlinx.android.synthetic.main.showcase_directory.*


class DirectoryShowcase : AppCompatActivity() {

    private val searcher = HitsSearcher(
        client = client,
        indexName = IndexName("mobile_demo_home"),
        query = Query(hitsPerPage = 100)
    )
    private val connection = ConnectionHandler()
    private val adapter = DirectoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showcase_directory)


        connection += searcher.connectHitsView(adapter) { response ->
            response.hits.deserialize(DirectoryHit.serializer())
                .filter { showcases.containsKey(it.objectID) }
                .groupBy { it.type }
                .toSortedMap()
                .flatMap { (key, value) ->
                    listOf(DirectoryItem.Header(key)) + value.map { DirectoryItem.Item(it) }.sortedBy { it.hit.objectID.raw }
                }
        }

        configureRecyclerView(hits, adapter)
        configureSearchView(searchView, getString(R.string.search_showcases))
        configureSearchBox(searchView, searcher, connection)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
