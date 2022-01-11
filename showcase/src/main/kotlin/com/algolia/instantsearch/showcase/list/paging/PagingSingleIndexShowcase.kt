package com.algolia.instantsearch.showcase.list.paging

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.android.list.HitsSearcherDataSource
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxConnectorPagedList
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.android.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.*
import com.algolia.instantsearch.showcase.databinding.IncludeSearchBinding
import com.algolia.instantsearch.showcase.databinding.ShowcasePagingBinding
import com.algolia.instantsearch.showcase.list.movie.Movie
import com.algolia.instantsearch.showcase.list.movie.MovieAdapterPaged

class PagingSingleIndexShowcase : AppCompatActivity() {

    private val searcher = HitsSearcher(client, stubIndexName)
    private val dataSourceFactory = HitsSearcherDataSource.Factory(searcher) {
        it.deserialize(Movie.serializer())
    }
    private val pagedListConfig =
        PagedList.Config.Builder().setPageSize(10).setEnablePlaceholders(false).build()
    private val movies = LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()
    private val searchBox = SearchBoxConnectorPagedList(searcher, listOf(movies))
    private val connection = ConnectionHandler(searchBox)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ShowcasePagingBinding.inflate(layoutInflater)
        val searchBinding = IncludeSearchBinding.bind(binding.searchBox.root)
        setContentView(binding.root)

        val adapter = MovieAdapterPaged()
        val searchBoxView = SearchBoxViewAppCompat(searchBinding.searchView)

        connection += searchBox.connectView(searchBoxView)

        movies.observe(this) { hits -> adapter.submitList(hits) }

        configureToolbar(binding.toolbar)
        configureSearcher(searcher)
        configureSearchView(searchBinding.searchView, getString(R.string.search_movies))
        configureRecyclerView(binding.hits, adapter)
        onResponseChangedThenUpdateNbHits(searcher, binding.nbHits, connection)
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
