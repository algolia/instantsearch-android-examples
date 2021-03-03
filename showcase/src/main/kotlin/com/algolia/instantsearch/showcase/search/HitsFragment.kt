package com.algolia.instantsearch.showcase.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.helper.android.list.autoScrollToStart
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.showcase.list.movie.Movie
import com.algolia.instantsearch.showcase.list.movie.MovieAdapter
import com.algolia.search.helper.deserialize
import kotlinx.android.synthetic.main.showcase_search_hits_fragment.*

class HitsFragment : Fragment(R.layout.showcase_search_hits_fragment) {

    private val connection = ConnectionHandler()
    private val parentActivity: SearchAutoCompleteTextView
        get() = activity as SearchAutoCompleteTextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MovieAdapter()
        connection += parentActivity.searcher.connectHitsView(adapter) { response ->
            response.hits.deserialize(Movie.serializer())
        }

        hits.let {
            it.visibility = View.VISIBLE
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
            it.itemAnimator = null
            it.autoScrollToStart(adapter)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        connection.clear()
    }
}
