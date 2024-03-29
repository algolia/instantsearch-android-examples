package com.algolia.exchange.query.suggestions.hits

import androidx.lifecycle.ViewModel
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.hits.addHitsSearcher
import com.algolia.instantsearch.helper.searcher.multi.MultiSearcher
import com.algolia.search.client.ClientSearch
import com.algolia.search.helper.deserialize
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query
import io.ktor.client.features.logging.*

class MainViewModel : ViewModel() {

    private val client = ClientSearch(
        ApplicationID("latency"),
        APIKey("afc3dd66dd1293e2e2736a5a51b05c0a"),
        LogLevel.ALL
    )

    private val multiSearcher = MultiSearcher(client)
    private val suggestionsSearcher = multiSearcher.addHitsSearcher(
        IndexName("instantsearch_query_suggestions"),
        Query(hitsPerPage = 3)
    )
    private val hitsSearchers = multiSearcher.addHitsSearcher(IndexName("instant_search"))
    private val searchBoxConnector = SearchBoxConnector(multiSearcher)

    private val connections = ConnectionHandler(searchBoxConnector)

    val searchBoxState = SearchBoxState()
    val suggestionsState = HitsState<Suggestion>()
    val hitsState = HitsState<Product>()

    init {
        connections += searchBoxConnector.connectView(searchBoxState)
        connections += suggestionsSearcher.connectHitsView(suggestionsState) {
            it.hits.deserialize(Suggestion.serializer())
        }
        connections += hitsSearchers.connectHitsView(hitsState) {
            it.hits.deserialize(Product.serializer())
        }
        multiSearcher.searchAsync()
    }

    override fun onCleared() {
        super.onCleared()
        suggestionsSearcher.cancel()
        connections.clear()
    }
}
