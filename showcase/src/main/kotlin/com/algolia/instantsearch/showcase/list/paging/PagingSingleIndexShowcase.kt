package com.algolia.instantsearch.showcase.list.paging

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.android.list.SearcherSingleIndexDataSource
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxConnectorPagedList
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.android.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.showcase.configureRecyclerView
import com.algolia.instantsearch.showcase.configureSearchView
import com.algolia.instantsearch.showcase.configureSearcher
import com.algolia.instantsearch.showcase.configureToolbar
import com.algolia.instantsearch.showcase.list.movie.Movie
import com.algolia.instantsearch.showcase.list.movie.MovieAdapterPaged
import com.algolia.instantsearch.showcase.onResponseChangedThenUpdateNbHits
import com.algolia.instantsearch.showcase.stubIndex
import kotlinx.android.synthetic.main.include_search.*
import kotlinx.android.synthetic.main.showcase_paging.*


class PagingSingleIndexShowcase : AppCompatActivity() {

    private val searcher = SearcherSingleIndex(stubIndex)
    private val dataSourceFactory = SearcherSingleIndexDataSource.Factory(
            searcher = searcher,
            triggerSearchForQuery = { query -> !query.query.isNullOrEmpty() },
            transformer = { it.deserialize(Movie.serializer()) }
    )
    private val pagedListConfig = PagedList.Config.Builder().setPageSize(10).setEnablePlaceholders(false).build()
    private val movies = LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()
    private val searchBox = SearchBoxConnectorPagedList(searcher, listOf(movies))
    private val connection = ConnectionHandler(searchBox)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showcase_paging)

        val adapter = MovieAdapterPaged()
        val searchBoxView = SearchBoxViewAppCompat(searchView)

        connection += searchBox.connectView(searchBoxView)

        movies.observe(this, Observer { hits -> adapter.submitList(hits) })

        configureToolbar(toolbar)
        configureSearcher(searcher)
        configureSearchView(searchView, getString(R.string.search_movies))
        configureRecyclerView(hits, adapter)
        onResponseChangedThenUpdateNbHits(searcher, nbHits, connection)
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
