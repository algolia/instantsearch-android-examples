package com.algolia.exchange.query.suggestions.categories

import android.util.Log
import androidx.lifecycle.ViewModel
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.helper.filter.facet.FacetListConnector
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.connectSearcher
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.instantsearch.helper.searcher.facets.addFacetsSearcher
import com.algolia.instantsearch.helper.searcher.hits.addHitsSearcher
import com.algolia.instantsearch.helper.searcher.multi.MultiSearcher
import com.algolia.search.client.ClientSearch
import com.algolia.search.helper.deserialize
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Facet
import io.ktor.client.features.logging.*

class MainViewModel : ViewModel() {

    private val client = ClientSearch(
        ApplicationID("latency"),
        APIKey("afc3dd66dd1293e2e2736a5a51b05c0a"),
        LogLevel.ALL
    )
    private val multiSearcher = MultiSearcher(client)
    private val suggestionsSearcher = multiSearcher.addHitsSearcher(
        indexName = IndexName("instantsearch_query_suggestions")
    )
    private val attribute = Attribute("categories")
    private val categoriesSearcher = multiSearcher.addFacetsSearcher(
        indexName = IndexName("instant_search"),
        attribute = attribute
    )
    private val searchBoxConnector = SearchBoxConnector(multiSearcher)

    private val connections = ConnectionHandler(searchBoxConnector)

    val searchBoxState = SearchBoxState()
    val categoriesState = HitsState<Facet>()
    val suggestionsState = HitsState<Suggestion>()

    init {
        connections += searchBoxConnector.connectView(searchBoxState)
        connections += categoriesSearcher.connectHitsView(categoriesState) { it.facets }
        connections += suggestionsSearcher.connectHitsView(suggestionsState) { it.hits.deserialize(Suggestion.serializer()) }
        multiSearcher.searchAsync()
    }

    override fun onCleared() {
        super.onCleared()
        multiSearcher.cancel()
        connections.clear()
    }
}
