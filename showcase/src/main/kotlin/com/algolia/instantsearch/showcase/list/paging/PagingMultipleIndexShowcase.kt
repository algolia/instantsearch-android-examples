package com.algolia.instantsearch.showcase.list.paging

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.android.list.SearcherSingleIndexDataSource
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxConnectorPagedList
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.android.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.instantsearch.helper.searcher.hits.addHitsSearcher
import com.algolia.instantsearch.helper.searcher.multi.MultiSearcher
import com.algolia.instantsearch.showcase.*
import com.algolia.instantsearch.showcase.databinding.IncludeSearchBinding
import com.algolia.instantsearch.showcase.databinding.ShowcaseSearchBinding
import com.algolia.instantsearch.showcase.list.actor.Actor
import com.algolia.instantsearch.showcase.list.actor.ActorAdapterNested
import com.algolia.instantsearch.showcase.list.movie.Movie
import com.algolia.instantsearch.showcase.list.movie.MovieAdapterNested
import com.algolia.search.model.IndexName
import kotlinx.serialization.DeserializationStrategy

class PagingMultipleIndexShowcase : AppCompatActivity() {

    private val multiSearcher = MultiSearcher(client)
    private val moviesSearcher = multiSearcher.addHitsSearcher(IndexName("mobile_demo_movies"))
    private val actorsSearcher = multiSearcher.addHitsSearcher(IndexName("mobile_demo_actors"))

    private val pagedListConfig: PagedList.Config = PagedList.Config.Builder().setPageSize(10).setEnablePlaceholders(false).build()
    private val movies = pagedLiveDataOf(moviesSearcher, pagedListConfig, Movie.serializer())
    private val actors = pagedLiveDataOf(actorsSearcher, pagedListConfig, Actor.serializer())

    private val searchBox = SearchBoxConnectorPagedList(multiSearcher, listOf(movies, actors))
    private val connection = ConnectionHandler(searchBox)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ShowcaseSearchBinding.inflate(layoutInflater)
        val searchBinding = IncludeSearchBinding.bind(binding.searchBox.root)
        setContentView(binding.root)

        val adapterActor = ActorAdapterNested()
        val adapterMovie = MovieAdapterNested()
        val adapter = PagingMultipleIndexAdapter()

        actors.observe(this, adapterActor::submitList)
        movies.observe(this, adapterMovie::submitList)

        adapter.submitList(
            listOf(
                PagingMultipleIndexItem.Header("Movies"),
                PagingMultipleIndexItem.Movies(adapterMovie),
                PagingMultipleIndexItem.Header("Actors"),
                PagingMultipleIndexItem.Actors(adapterActor)
            )
        )

        val searchBoxView = SearchBoxViewAppCompat(searchBinding.searchView)
        connection += searchBox.connectView(searchBoxView)

        configureToolbar(binding.toolbar)
        configureSearchView(searchBinding.searchView, getString(R.string.search_movies))
        configureRecyclerView(binding.hits, adapter)
    }

    private fun <T> pagedLiveDataOf(
        searcher: HitsSearcher,
        config: PagedList.Config,
        serializer: DeserializationStrategy<T>,
    ): LiveData<PagedList<T>> {
        val factory = SearcherSingleIndexDataSource.Factory(searcher) { it.deserialize(serializer) }
        return LivePagedListBuilder(factory, config).build()
    }

    override fun onDestroy() {
        super.onDestroy()
        multiSearcher.cancel()
        connection.clear()
    }
}
