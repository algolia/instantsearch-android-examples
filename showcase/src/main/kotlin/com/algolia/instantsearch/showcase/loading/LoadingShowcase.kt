package com.algolia.instantsearch.showcase.loading

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingConfig
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.android.list.Paginator
import com.algolia.instantsearch.helper.android.list.liveData
import com.algolia.instantsearch.helper.android.loading.LoadingViewSwipeRefreshLayout
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.android.searchbox.connectPaginator
import com.algolia.instantsearch.helper.loading.LoadingConnector
import com.algolia.instantsearch.helper.loading.connectView
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.*
import com.algolia.instantsearch.showcase.databinding.IncludeSearchBinding
import com.algolia.instantsearch.showcase.databinding.ShowcaseLoadingBinding
import com.algolia.instantsearch.showcase.list.movie.Movie
import com.algolia.instantsearch.showcase.list.movie.MovieAdapterPaged

class LoadingShowcase : AppCompatActivity() {

    private val searcher = HitsSearcher(client, stubIndexName)
    private val paginator = Paginator(
        searcher = searcher,
        pagingConfig = PagingConfig(pageSize = 10, enablePlaceholders = false)
    ) { hit -> hit.deserialize(Movie.serializer()) }
    private val loading = LoadingConnector(searcher)
    private val searchBox = SearchBoxConnector(searcher)
    private val connection = ConnectionHandler(loading, searchBox)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ShowcaseLoadingBinding.inflate(layoutInflater)
        val searchBinding = IncludeSearchBinding.bind(binding.searchBox.root)
        setContentView(binding.root)

        val view = LoadingViewSwipeRefreshLayout(binding.swipeRefreshLayout)
        val searchBoxView = SearchBoxViewAppCompat(searchBinding.searchView)
        val adapter = MovieAdapterPaged()

        connection += loading.connectView(view)
        connection += searchBox.connectView(searchBoxView)
        connection += searchBox.connectPaginator(paginator)

        paginator.liveData.observe(this) { pagingData -> adapter.submitData(lifecycle, pagingData) }

        configureSearcher(searcher)
        configureToolbar(binding.toolbar)
        configureSearchView(searchBinding.searchView, getString(R.string.search_movies))
        configureRecyclerView(binding.hits, adapter)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
