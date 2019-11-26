package com.algolia.instantsearch.showcase.sortby

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.showcase.client
import com.algolia.instantsearch.showcase.configureRecyclerView
import com.algolia.instantsearch.showcase.configureToolbar
import com.algolia.instantsearch.showcase.list.movie.Movie
import com.algolia.instantsearch.showcase.list.movie.MovieAdapter
import com.algolia.instantsearch.helper.android.sortby.SortByViewAutocomplete
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.sortby.SortByConnector
import com.algolia.instantsearch.helper.sortby.connectView
import com.algolia.search.helper.deserialize
import com.algolia.search.model.IndexName
import kotlinx.android.synthetic.main.showcase_sort_by.*


class SortByShowcase : AppCompatActivity() {

    private val indexTitle = client.initIndex(IndexName("mobile_demo_movies"))
    private val indexYearAsc = client.initIndex(IndexName("mobile_demo_movies_year_asc"))
    private val indexYearDesc = client.initIndex(IndexName("mobile_demo_movies_year_desc"))
    private val searcher = SearcherSingleIndex(indexTitle)
    private val indexes = mapOf(
        0 to indexTitle,
        1 to indexYearAsc,
        2 to indexYearDesc
    )
    private val sortBy = SortByConnector(indexes, searcher, selected = 0)
    private val connection = ConnectionHandler(sortBy)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showcase_sort_by)

        val adapter = ArrayAdapter<String>(this, R.layout.menu_item)
        val view = SortByViewAutocomplete(autoCompleteTextView, adapter)
        val adapterMovie = MovieAdapter()

        connection += sortBy.connectView(view) { index ->
            when (index) {
                indexTitle -> "Default"
                indexYearAsc -> "Year Asc"
                indexYearDesc -> "Year Desc"
                else -> index.indexName.raw
            }
        }
        connection += searcher.connectHitsView(adapterMovie) { response ->
            response.hits.deserialize(Movie.serializer())
        }

        configureToolbar(toolbar)
        configureRecyclerView(list, adapterMovie)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.disconnect()
    }
}