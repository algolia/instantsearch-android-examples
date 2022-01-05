package com.algolia.instantsearch.showcase.list.paging

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.android.list.SearcherSingleIndexDataSource
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxConnectorPagedList
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.android.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.*
import com.algolia.instantsearch.showcase.list.movie.Movie
import com.algolia.instantsearch.showcase.list.movie.MovieAdapterPaged
import kotlinx.android.synthetic.main.include_search.*
import kotlinx.android.synthetic.main.showcase_paging.*


class PagingSingleIndexShowcase : AppCompatActivity() {

    private val searcher = HitsSearcher(client, stubIndexName)
    private val dataSourceFactory =
        SearcherSingleIndexDataSource.Factory(searcher) { it.deserialize(Movie.serializer()) }
    private val pagedListConfig =
        PagedList.Config.Builder().setPageSize(10).setEnablePlaceholders(false).build()
    private val movies = LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()
    private val searchBox = SearchBoxConnectorPagedList(searcher, listOf(movies))
    private val connection = ConnectionHandler(searchBox)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showcase_paging)

        val adapter = MovieAdapterPaged()
        val searchBoxView = SearchBoxViewAppCompat(searchView)

        connection += searchBox.connectView(searchBoxView)

        movies.observe(this) { hits -> adapter.submitList(hits) }

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
