package com.algolia.instantsearch.showcase.stats

import android.os.Bundle
import android.text.SpannedString
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.showcase.*
import com.algolia.instantsearch.helper.android.stats.StatsTextView
import com.algolia.instantsearch.helper.android.stats.StatsTextViewSpanned
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.instantsearch.helper.stats.StatsConnector
import com.algolia.instantsearch.helper.stats.StatsPresenter
import com.algolia.instantsearch.helper.stats.StatsPresenterImpl
import com.algolia.instantsearch.helper.stats.connectView
import kotlinx.android.synthetic.main.showcase_paging.toolbar
import kotlinx.android.synthetic.main.showcase_stats.*
import kotlinx.android.synthetic.main.include_search.*


class StatsShowcase : AppCompatActivity() {

    private val searcher = HitsSearcher(client, stubIndexName)
    private val stats = StatsConnector(searcher)
    private val connection = ConnectionHandler(stats)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showcase_stats)

        val statsViewA = StatsTextView(statsA)
        val statsViewB = StatsTextViewSpanned(statsB)
        val presenter: StatsPresenter<SpannedString> = { response ->
            buildSpannedString {
                if (response != null) {
                    bold { append(response.nbHits.toString()) }
                    append(" ${getString(R.string.hits)}")
                    val query = searcher.query.query

                    if (query != null && query.isNotBlank()) {
                        append(" for \"$query\"")
                    }
                }
            }
        }

        connection += stats.connectView(statsViewA, StatsPresenterImpl())
        connection += stats.connectView(statsViewB, presenter)

        configureToolbar(toolbar)
        configureSearcher(searcher)
        configureSearchView(searchView, getString(R.string.search_movies))
        configureSearchBox(searchView, searcher, connection)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}