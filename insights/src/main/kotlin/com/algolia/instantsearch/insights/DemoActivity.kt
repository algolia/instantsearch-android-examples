package com.algolia.instantsearch.insights

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.helper.android.list.autoScrollToStart
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.SearchMode
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.tracker.HitsTracker
import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.helper.deserialize
import com.algolia.search.model.insights.EventName
import io.ktor.client.features.logging.LogLevel
import kotlinx.android.synthetic.main.demo_activity.*

class DemoActivity : AppCompatActivity() {

    private val client = ClientSearch(
        ConfigurationSearch(
            applicationID = App.APP_ID,
            apiKey = App.API_KEY,
            logLevel = LogLevel.ALL
        )
    )
    private val stubIndex = client.initIndex(App.INDEX_NAME)
    private val searcher = SearcherSingleIndex(stubIndex)
    private val searchBox = SearchBoxConnector(searcher, searchMode = SearchMode.OnSubmit)
    private val connection = ConnectionHandler(searchBox)

    private val insights = Insights.register(
        context = applicationContext,
        appId = App.APP_ID,
        apiKey = App.API_KEY,
        indexName = App.INDEX_NAME
    )
    private val hitsTracker = HitsTracker(
        eventName = EventName("demo"),
        searcher = searcher,
        insights = insights
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity)

        val adapter = ListItemAdapter()
        recyclerView.let {
            it.visibility = View.VISIBLE
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
            it.itemAnimator = null
            it.autoScrollToStart(adapter)
        }

        val searchBoxView = SearchBoxViewAppCompat(searchView)
        connection += searchBox.connectView(searchBoxView)
        connection += searcher.connectHitsView(adapter) { response ->
            response.hits.deserialize(ListItem.serializer())
        }

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
