package com.algolia.exchange.multi.index

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
        APIKey("1f6fd3a6fb973cb08419fe7d288fa4db"),
        LogLevel.ALL
    )
    private val multiSearcher = MultiSearcher(client)
    private val actorsSearcher = multiSearcher.addHitsSearcher(IndexName("mobile_demo_actors"), Query(hitsPerPage = 5))
    private val moviesSearcher = multiSearcher.addHitsSearcher(IndexName("mobile_demo_movies"))
    private val searchBoxConnector = SearchBoxConnector(multiSearcher)
    private val connections = ConnectionHandler(searchBoxConnector)

    val searchBoxState = SearchBoxState()
    val actorsState = HitsState<Actor>()
    val moviesState = HitsState<Movie>()

    init {
        connections += searchBoxConnector.connectView(searchBoxState)
        connections += actorsSearcher.connectHitsView(actorsState) { it.hits.deserialize(Actor.serializer()) }
        connections += moviesSearcher.connectHitsView(moviesState) { it.hits.deserialize(Movie.serializer()) }
        multiSearcher.searchAsync()
    }

    override fun onCleared() {
        super.onCleared()
        multiSearcher.cancel()
        connections.clear()
    }
}
