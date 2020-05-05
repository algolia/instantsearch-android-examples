package com.algolia.instantsearch.showcase.relateditems

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.showcase.configureRecyclerView
import com.algolia.instantsearch.showcase.configureSearcher
import com.algolia.instantsearch.showcase.configureToolbar
import com.algolia.instantsearch.showcase.directory.DirectoryItem
import com.algolia.instantsearch.showcase.stubIndex
import com.algolia.search.helper.deserialize
import kotlinx.android.synthetic.main.showcase_relateditems.toolbar
import kotlinx.android.synthetic.main.showcase_search.list

class RelatedItemsShowcase : AppCompatActivity() {

    private val searcher = SearcherSingleIndex(stubIndex)
    private val connection = ConnectionHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showcase_relateditems)

        val adapter = ProductAdapter()
        connection += searcher.connectHitsView(adapter) { response ->
            response.hits.deserialize(Product.serializer())
        }

        configureSearcher(searcher)
        configureToolbar(toolbar)
        configureRecyclerView(list, adapter)

        searcher.query.hitsPerPage = 3
        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }
}
