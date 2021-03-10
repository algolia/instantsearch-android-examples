package com.algolia.instantsearch.showcase.sortby

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.android.list.SearcherSingleIndexDataSource
import com.algolia.instantsearch.helper.android.loading.LoadingViewSwipeRefreshLayout
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxConnectorPagedList
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.android.searchbox.connectView
import com.algolia.instantsearch.helper.android.sortby.SortByViewAutocomplete
import com.algolia.instantsearch.helper.loading.LoadingConnector
import com.algolia.instantsearch.helper.loading.connectView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.sortby.SortByConnector
import com.algolia.instantsearch.helper.sortby.connectView
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.showcase.client
import com.algolia.instantsearch.showcase.configureRecyclerView
import com.algolia.instantsearch.showcase.configureSearchView
import com.algolia.instantsearch.showcase.configureToolbar
import com.algolia.instantsearch.showcase.list.movie.Movie
import com.algolia.instantsearch.showcase.list.movie.MovieAdapterPaged
import com.algolia.search.model.IndexName
import kotlinx.android.synthetic.main.include_search.*
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

    private val dataSourceFactory =
        SearcherSingleIndexDataSource.Factory(searcher) { it.deserialize(Movie.serializer()) }
    private val pagedListConfig =
        PagedList.Config.Builder().setPageSize(10).setEnablePlaceholders(false).build()
    private val movies = LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()
    private val loading = LoadingConnector(searcher)
    private val searchBox = SearchBoxConnectorPagedList(searcher, listOf(movies))

    private val connection = ConnectionHandler(sortBy, loading, searchBox)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showcase_sort_by)

        val arrayAdapter = ArrayAdapter<String>(this, R.layout.menu_item)
        val sortView = SortByViewAutocomplete(autoCompleteTextView, arrayAdapter)

        val view = LoadingViewSwipeRefreshLayout(swipeRefreshLayout)
        val searchBoxView = SearchBoxViewAppCompat(searchView)
        val adapter = MovieAdapterPaged()
        connection += loading.connectView(view)
        connection += searchBox.connectView(searchBoxView)
        movies.observe(this) { hits -> adapter.submitList(hits) }

        connection += sortBy.connectView(sortView) { index ->
            when (index) {
                indexTitle -> "Default"
                indexYearAsc -> "Year Asc"
                indexYearDesc -> "Year Desc"
                else -> index.indexName.raw
            }
        }

        sortBy.viewModel.eventSelection.subscribe {
            adapter.submitList(null)
            searchBoxView.onQuerySubmitted?.invoke(searchView.query.toString())
        }

        configureToolbar(toolbar)
        configureRecyclerView(hits, adapter)
        configureSearchView(searchView, getString(R.string.search_movies))

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
