package com.algolia.instantsearch.examples.querysuggestions

//TODO: Move to e.g. BuildConfig
import android.util.Log
import com.algolia.instantsearch.examples.querysuggestions.QuerySuggestionsActivity.Companion.ALGOLIA_API_KEY
import com.algolia.instantsearch.examples.querysuggestions.QuerySuggestionsActivity.Companion.ALGOLIA_APP_ID
import com.algolia.instantsearch.examples.querysuggestions.QuerySuggestionsActivity.Companion.ALGOLIA_INDEX_SUGGESTIONS
import com.algolia.instantsearch.helpers.QuerySuggestionsContentProvider
import com.algolia.instantsearch.helpers.Searcher
import com.algolia.search.saas.Index

class QSContentProvider : QuerySuggestionsContentProvider() {
    override fun getLimit(): Int {
        return 4
    }

    override fun getIndex(): Index? {
        Log.d("QS_CP", "Creating index.")
        return Searcher.create(ALGOLIA_APP_ID, ALGOLIA_API_KEY, ALGOLIA_INDEX_SUGGESTIONS, "suggestions").index
    }
}
