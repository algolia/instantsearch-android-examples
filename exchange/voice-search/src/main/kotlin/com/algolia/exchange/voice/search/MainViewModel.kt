package com.algolia.exchange.voice.search

import androidx.lifecycle.ViewModel
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.search.client.ClientSearch
import com.algolia.search.helper.deserialize
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import io.ktor.client.features.logging.*

class MainViewModel : ViewModel() {

    private val client = ClientSearch(
        ApplicationID("latency"),
        APIKey("1f6fd3a6fb973cb08419fe7d288fa4db"),
        LogLevel.ALL
    )
    private val hitsSearcher = HitsSearcher(client, IndexName("instant_search"))
    private val searchBoxConnector = SearchBoxConnector(hitsSearcher)
    private val connections = ConnectionHandler(searchBoxConnector)

    val searchBoxState = SearchBoxState()
    val productsState = HitsState<Product>()

    init {
        connections += searchBoxConnector.connectView(searchBoxState)
        connections += hitsSearcher.connectHitsView(productsState) { it.hits.deserialize(Product.serializer()) }
        hitsSearcher.searchAsync()
    }

    override fun onCleared() {
        super.onCleared()
        hitsSearcher.cancel()
        connections.clear()
    }

    fun setQuery(query: String?) {
        if (query == null) return
        searchBoxState.setText(query, true)
    }
}
