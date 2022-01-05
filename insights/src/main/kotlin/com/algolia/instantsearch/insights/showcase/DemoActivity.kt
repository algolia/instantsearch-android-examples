package com.algolia.instantsearch.insights.showcase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.SearchMode
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.instantsearch.helper.tracker.HitsTracker
import com.algolia.instantsearch.insights.sharedInsights
import com.algolia.instantsearch.insights.showcase.App.Companion.IndexName
import com.algolia.instantsearch.insights.showcase.extension.configureRecyclerView
import com.algolia.instantsearch.insights.showcase.extension.configureSearchView
import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.helper.deserialize
import com.algolia.search.model.insights.EventName
import io.ktor.client.features.logging.*
import kotlinx.android.synthetic.main.demo_activity.*
import kotlinx.android.synthetic.main.include_search.*

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity)
        setSupportActionBar(toolbar)

        val adapter = ListItemAdapter(hitsTracker)
        val searchBoxView = SearchBoxViewAppCompat(searchView)
        connection += searchBox.connectView(searchBoxView)
        connection += searcher.connectHitsView(adapter) { response ->
            response.hits
                .deserialize(ListItem.serializer())
                .mapIndexed { index, listItem -> ItemModel(listItem, index + 1) }
        }

        configureSearchView(searchView, resources.getString(R.string.search_items))
        configureRecyclerView(adapter)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
