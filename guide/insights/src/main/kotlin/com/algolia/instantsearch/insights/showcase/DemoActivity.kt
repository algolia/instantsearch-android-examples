package com.algolia.instantsearch.insights.showcase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.SearchMode
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.instantsearch.tracker.HitsTracker
import com.algolia.instantsearch.insights.sharedInsights
import com.algolia.instantsearch.insights.showcase.App.Companion.IndexName
import com.algolia.instantsearch.insights.showcase.databinding.DemoActivityBinding
import com.algolia.instantsearch.insights.showcase.databinding.IncludeSearchBinding
import com.algolia.instantsearch.insights.showcase.extension.configureRecyclerView
import com.algolia.instantsearch.insights.showcase.extension.configureSearchView
import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.helper.deserialize
import com.algolia.search.model.insights.EventName
import io.ktor.client.features.logging.*

class DemoActivity : AppCompatActivity() {

    private val client = ClientSearch(
        ConfigurationSearch(
            applicationID = App.AppID,
            apiKey = App.ApiKey,
            logLevel = LogLevel.ALL
        )
    )
    private val searcher = HitsSearcher(client, IndexName)
    private val searchBox = SearchBoxConnector(searcher, searchMode = SearchMode.AsYouType)

    private val hitsTracker = HitsTracker(
        eventName = EventName("demo"),
        searcher = searcher,
        insights = sharedInsights(IndexName)
    )
    private val connection = ConnectionHandler(searchBox, hitsTracker)

    private lateinit var binding: DemoActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DemoActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val adapter = ListItemAdapter(hitsTracker)
        val searchBinding = IncludeSearchBinding.bind(binding.searchbox.root)
        val searchBoxView = SearchBoxViewAppCompat(searchBinding.searchView)

        connection += searchBox.connectView(searchBoxView)
        connection += searcher.connectHitsView(adapter) { response ->
            response.hits
                .deserialize(ListItem.serializer())
                .mapIndexed { index, listItem -> ItemModel(listItem, index + 1) }
        }

        configureSearchView(searchBinding.searchView, resources.getString(R.string.search_items))
        configureRecyclerView(binding.recyclerView, adapter)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
