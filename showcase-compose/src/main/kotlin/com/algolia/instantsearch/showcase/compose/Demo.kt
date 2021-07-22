package com.algolia.instantsearch.showcase.compose

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.compose.searchbox.SearchQuery
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.serialize.KeyIndexName
import io.ktor.client.features.logging.*

val client = ClientSearch(
    ConfigurationSearch(
        ApplicationID("latency"),
        APIKey("1f6fd3a6fb973cb08419fe7d288fa4db"),
        logLevel = LogLevel.ALL
    )
)

val stubIndex = client.initIndex(IndexName("stub"))

fun AppCompatActivity.configureSearcher(searcher: SearcherSingleIndex) {
    searcher.index = client.initIndex(intent.indexName)
}

fun AppCompatActivity.configureSearcher(searcher: SearcherForFacets) {
    searcher.index = client.initIndex(intent.indexName)
}

val Intent.indexName: IndexName get() = IndexName(extras!!.getString(KeyIndexName)!!)

fun <R> configureSearchBox(
    searcher: Searcher<R>,
    searcherQuery: SearchQuery,
    connections: ConnectionHandler
) {
    SearchBoxConnector(searcher).also {
        connections += it
        connections += it.connectView(searcherQuery)
    }
}
