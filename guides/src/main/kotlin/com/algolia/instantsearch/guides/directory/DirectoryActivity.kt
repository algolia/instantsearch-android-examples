package com.algolia.instantsearch.guides.directory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.guides.R
import com.algolia.instantsearch.helper.android.list.autoScrollToStart
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.client.ClientSearch
import com.algolia.search.helper.deserialize
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import kotlinx.android.synthetic.main.activity_query_suggestion.*


class DirectoryActivity : AppCompatActivity() {

    private val client = ClientSearch(ApplicationID("latency"), APIKey("1f6fd3a6fb973cb08419fe7d288fa4db"))
    private val index = client.initIndex(IndexName("mobile_guides"))
    private val searcher = SearcherSingleIndex(index)
    private val connector = SearchBoxConnector(searcher)
    private val connection = ConnectionHandler(connector)
    private val adapter = DirectoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_directory)


        connection += searcher.connectHitsView(adapter) { response ->
            response.hits.deserialize(DirectoryHit.serializer())
                .filter { guides.containsKey(it.objectID) }
                .groupBy { it.type }
                .toSortedMap()
                .flatMap { (key, value) ->
                    listOf(DirectoryItem.Header(key)) + value.map { DirectoryItem.Item(it) }.sortedBy { it.hit.objectID.raw }
                }
        }
        connection += connector.connectView(SearchBoxViewAppCompat(searchView))

        list.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
            it.itemAnimator = null
            it.autoScrollToStart(adapter)
        }

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.disconnect()
    }
}