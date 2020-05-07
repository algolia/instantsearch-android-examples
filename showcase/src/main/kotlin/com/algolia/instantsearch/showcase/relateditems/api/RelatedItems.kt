package com.algolia.instantsearch.showcase.relateditems.api

import com.algolia.instantsearch.core.Presenter
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.showcase.relateditems.api.internal.extensions.RelatedItemsConnectionView
import com.algolia.search.model.indexing.Indexable
import com.algolia.search.model.response.ResponseSearch

public fun <T> SearcherSingleIndex.connectHitsView2(
    adapter: HitsView<T>,
    hit: T,
    matchingPatterns: List<MatchingPattern<T>>,
    presenter: Presenter<ResponseSearch, List<T>>
): Connection where T : Indexable {
    return RelatedItemsConnectionView(this, adapter, hit, matchingPatterns, presenter)
}
