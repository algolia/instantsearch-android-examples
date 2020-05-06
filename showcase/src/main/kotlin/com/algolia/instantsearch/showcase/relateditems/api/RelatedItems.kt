package com.algolia.instantsearch.showcase.relateditems.api

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.Presenter
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.showcase.relateditems.api.internal.extensions.toNegatedFacetFilter
import com.algolia.instantsearch.showcase.relateditems.api.internal.toOptionalFilters
import com.algolia.search.model.indexing.Indexable
import com.algolia.search.model.response.ResponseSearch

fun <T> SearcherSingleIndex.configureRelatedItems(hit: T, patterns: List<MatchingPattern<T>>) where T : Indexable {
    query.apply {
        sumOrFiltersScores = true
        facetFilters = hit.toNegatedFacetFilter()
        optionalFilters = patterns.toOptionalFilters(hit)
    }
}

public fun <T> SearcherSingleIndex.connectHitsView2(
    adapter: HitsView<T>,
    hit: T,
    matchingPatterns: List<MatchingPattern<T>>,
    presenter: Presenter<ResponseSearch, List<T>>
): Connection where T : Indexable {
    return RelatedItemsConnectionView(this, adapter, hit, matchingPatterns, presenter)
}

internal data class RelatedItemsConnectionView<T>(
    private val searcher: SearcherSingleIndex,
    private val view: HitsView<T>,
    private val hit: T,
    private val matchingPatterns: List<MatchingPattern<T>>,
    private val presenter: Presenter<ResponseSearch, List<T>>
) : ConnectionImpl() where T : Indexable {

    init {
        searcher.configureRelatedItems(hit, matchingPatterns)
    }

    private val callback: Callback<ResponseSearch?> = { response ->
        if (response != null) {
            view.setHits(presenter(response))
        }
    }

    override fun connect() {
        super.connect()
        searcher.response.subscribe(callback)
    }

    override fun disconnect() {
        super.disconnect()
        searcher.response.unsubscribe(callback)
    }
}
