package com.algolia.instantsearch.guides.places

import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.searcher.Sequencer
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.helper.searcher.SearcherScope
import com.algolia.search.client.ClientPlaces
import com.algolia.search.configuration.ConfigurationPlaces
import com.algolia.search.model.places.PlacesQuery
import com.algolia.search.model.response.ResponseSearchPlacesMono
import com.algolia.search.model.search.Language
import com.algolia.search.transport.RequestOptions
import io.ktor.client.features.logging.LogLevel
import kotlinx.coroutines.*


class SearcherPlaces(
    val client: ClientPlaces = ClientPlaces(),
    val language: Language = Language.English,
    val query: PlacesQuery = PlacesQuery(),
    val requestOptions: RequestOptions? = null,
    override val coroutineScope: CoroutineScope = SearcherScope(),
    override val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : Searcher<ResponseSearchPlacesMono> {

    private val sequencer = Sequencer()

    override val isLoading = SubscriptionValue(false)
    override val error = SubscriptionValue<Throwable?>(null)
    override val response = SubscriptionValue<ResponseSearchPlacesMono?>(null)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        error.value = throwable
    }

    override fun setQuery(text: String?) {
        query.query = text
    }

    override suspend fun search(): ResponseSearchPlacesMono {
        withContext(dispatcher) { isLoading.value = true }
        val response = client.searchPlaces(language, query, requestOptions)
        withContext(dispatcher) {
            isLoading.value = false
            this@SearcherPlaces.response.value = response
        }
        return response
    }

    override fun searchAsync(): Job {
        return coroutineScope.launch(dispatcher + exceptionHandler) {
            withContext(Dispatchers.Default) { search() }
        }.also {
            sequencer.addOperation(it)
        }
    }

    override fun cancel() {
        sequencer.cancelAll()
    }
}