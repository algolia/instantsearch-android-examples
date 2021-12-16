package com.algolia.exchange.querysuggestions

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
        APIKey("afc3dd66dd1293e2e2736a5a51b05c0a"),
        LogLevel.ALL
    )
    private val indexName = IndexName("instantsearch_query_suggestions")
    private val searcherSuggestion = HitsSearcher(client, indexName)
    private val searchBoxConnector = SearchBoxConnector(searcherSuggestion)
    private val connections = ConnectionHandler(searchBoxConnector)

    val searchBoxState = SearchBoxState()
    val suggestionsState = HitsState<Suggestion>()

    init {
        connections += searchBoxConnector.connectView(searchBoxState)
        connections += searcherSuggestion.connectHitsView(suggestionsState) {
            it.hits.deserialize(Suggestion.serializer())
        }
        searcherSuggestion.searchAsync()
    }

    override fun onCleared() {
        super.onCleared()
        searcherSuggestion.cancel()
        connections.clear()
    }
}
