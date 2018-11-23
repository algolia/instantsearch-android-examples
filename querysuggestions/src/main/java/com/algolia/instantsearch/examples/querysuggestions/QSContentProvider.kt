package com.algolia.instantsearch.examples.querysuggestions

import com.algolia.instantsearch.core.helpers.QuerySuggestionsContentProvider
import com.algolia.instantsearch.core.helpers.Searcher
import com.algolia.instantsearch.examples.querysuggestions.BuildConfig.*
import com.algolia.search.saas.Index

class QSContentProvider : QuerySuggestionsContentProvider() {
    override fun getLimit(): Int {
        return 5
    }

    override fun initIndex(): Index? {
        return Searcher.create(ALGOLIA_APP_ID, ALGOLIA_API_KEY, ALGOLIA_INDEX_SUGGESTIONS, "suggestions").index
    }

    override fun shouldReturnHighlightResult(): Boolean {
        return true
    }
}
