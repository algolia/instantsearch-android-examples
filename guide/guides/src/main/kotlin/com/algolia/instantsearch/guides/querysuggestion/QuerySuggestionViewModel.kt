package com.algolia.instantsearch.guides.querysuggestion

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.algolia.instantsearch.guides.querysuggestion.suggestion.Suggestion
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searcher.hits.addHitsSearcher
import com.algolia.instantsearch.searcher.multi.MultiSearcher
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import io.ktor.client.features.logging.*

class QuerySuggestionViewModel : ViewModel() {

    private val client = ClientSearch(
        applicationID = ApplicationID("latency"),
        apiKey = APIKey("afc3dd66dd1293e2e2736a5a51b05c0a"),
        logLevel = LogLevel.ALL
    )
    val multiSearcher = MultiSearcher(client)
    val productSearcher = multiSearcher.addHitsSearcher(indexName = IndexName("instant_search"))
    val suggestionSearcher = multiSearcher.addHitsSearcher(indexName = IndexName("instantsearch_query_suggestions"))
    val searchBox = SearchBoxConnector(multiSearcher)
    val suggestions = MutableLiveData<Suggestion>()

    override fun onCleared() {
        multiSearcher.cancel()
        searchBox.disconnect()
        client.close()
    }
}
