package com.algolia.instantsearch.showcase.loading

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.android.list.SearcherSingleIndexDataSource
import com.algolia.instantsearch.helper.android.loading.LoadingViewSwipeRefreshLayout
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxConnectorPagedList
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.android.searchbox.connectView
import com.algolia.instantsearch.helper.loading.LoadingConnector
import com.algolia.instantsearch.helper.loading.connectView
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.*
import com.algolia.instantsearch.showcase.list.movie.Movie
import com.algolia.instantsearch.showcase.list.movie.MovieAdapterPaged
import kotlinx.android.synthetic.main.include_search.*
import kotlinx.android.synthetic.main.showcase_loading.*
import kotlinx.android.synthetic.main.showcase_search.hits
import kotlinx.android.synthetic.main.showcase_search.toolbar


class LoadingShowcase : AppCompatActivity() {

    private val searcher = HitsSearcher(client, stubIndexName)
    private val dataSourceFactory = SearcherSingleIndexDataSource.Factory(searcher) { it.deserialize(Movie.serializer()) }
    private val pagedListConfig = PagedList.Config.Builder().setPageSize(10).setEnablePlaceholders(false).build()
    private val movies = LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()
    private val loading = LoadingConnector(searcher)
    private val searchBox = SearchBoxConnectorPagedList(searcher, listOf(movies))
    private val connection = ConnectionHandler(loading, searchBox)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showcase_loading)

        val view = LoadingViewSwipeRefreshLayout(swipeRefreshLayout)
        val searchBoxView = SearchBoxViewAppCompat(searchView)
        val adapter = MovieAdapterPaged()

        connection += loading.connectView(view)
        connection += searchBox.connectView(searchBoxView)
        movies.observe(this, Observer { hits -> adapter.submitList(hits) })

        configureSearcher(searcher)
        configureToolbar(toolbar)
        configureSearchView(searchView, getString(R.string.search_movies))
        configureRecyclerView(hits, adapter)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
