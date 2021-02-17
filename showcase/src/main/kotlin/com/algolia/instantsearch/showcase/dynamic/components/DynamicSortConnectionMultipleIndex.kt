package com.algolia.instantsearch.showcase.dynamic.components

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.search.model.response.ResponseSearches

internal class DynamicSortToggleConnectionMultipleIndex(
    val viewModel: DynamicSortViewModel,
    val searcher: SearcherMultipleIndex,
    private val queryIndex: Int
) : ConnectionImpl() {

    private val priorityCallback: Callback<DynamicSortPriority?> = callback@{ priority ->
        if (priority == null) return@callback
        searcher.queries[queryIndex].query.relevancyStrictness = priority.relevancyStrictness
        searcher.searchAsync()
    }

    private val responseCallback: Callback<ResponseSearches?> = callback@{ responses ->
        if (responses == null) return@callback
        val receivedRelevancyStrictness =
            responses.results[queryIndex].appliedRelevancyStrictnessOrNull ?: run {
                viewModel.priority.value = null
                return@callback
            }
        val dynamicSortPriority = DynamicSortPriority.of(receivedRelevancyStrictness)
        if (dynamicSortPriority != viewModel.priority.value) {
            viewModel.priority.value = dynamicSortPriority
        }
    }

    override fun connect() {
        super.connect()
        viewModel.priority.subscribe(priorityCallback)
        searcher.response.subscribePast(responseCallback)
    }

    override fun disconnect() {
        super.disconnect()
        viewModel.priority.unsubscribe(priorityCallback)
        searcher.response.unsubscribe(responseCallback)
    }
}

/**
 * Create a connection between a view model and a searcher.
 *
 * @param searcher searcher to connect
 * @param queryIndex query index
 */
public fun DynamicSortViewModel.connectSearcher(
    searcher: SearcherMultipleIndex,
    queryIndex: Int
): Connection {
    return DynamicSortToggleConnectionMultipleIndex(this, searcher, queryIndex)
}
