package com.algolia.instantsearch.examples.querysuggestions

import com.algolia.instantsearch.examples.querysuggestions.BuildConfig.*
import com.algolia.instantsearch.helpers.QuerySuggestionsContentProvider
import com.algolia.instantsearch.helpers.Searcher
import com.algolia.search.saas.Index

class QSContentProvider : QuerySuggestionsContentProvider() {
    override fun getLimit(): Int {
        return 4
    }

    override fun getIndex(): Index? {
        return Searcher.create(ALGOLIA_APP_ID, ALGOLIA_API_KEY, ALGOLIA_INDEX_SUGGESTIONS, "suggestions").index
    }
}
