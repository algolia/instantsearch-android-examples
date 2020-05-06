package com.algolia.instantsearch.showcase.relateditems

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.showcase.configureRecyclerView
import com.algolia.instantsearch.showcase.configureSearcher
import com.algolia.instantsearch.showcase.configureToolbar
import com.algolia.instantsearch.showcase.relateditems.api.MatchingPattern
import com.algolia.instantsearch.showcase.relateditems.api.connectHitsView2
import com.algolia.instantsearch.showcase.stubIndex
import com.algolia.search.helper.deserialize
import com.algolia.search.model.Attribute
import kotlinx.android.synthetic.main.showcase_relateditems.hits
import kotlinx.android.synthetic.main.showcase_relateditems.relatedItems
import kotlinx.android.synthetic.main.showcase_relateditems.toolbar

class RelatedItemsShowcase : AppCompatActivity() {

    private val searcher = SearcherSingleIndex(stubIndex)
    private val relatedItemsSearcher = SearcherSingleIndex(stubIndex)
    private val connection = ConnectionHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showcase_relateditems)
        searcher.query.hitsPerPage = 3 // Limit to 3 results

        configureToolbar(toolbar)
        configureSearcher(searcher)
        configureSearcher(relatedItemsSearcher)

        val hitsAdapter = ProductAdapter()
        connection += searcher.connectHitsView(hitsAdapter) { response ->
            response.hits.deserialize(Product.serializer())
        }
        configureRecyclerView(hits, hitsAdapter)

        val relatedItemsAdapter = ProductAdapter()
        configureRecyclerView(relatedItems, relatedItemsAdapter)

        val matchingPatterns: List<MatchingPattern<Product>> = listOf(
            MatchingPattern(Attribute("brand"), 1, Product::brand),
            MatchingPattern(Attribute("categories"), 2, Product::categories)
        )

        hitsAdapter.callback = { product ->
            connection += relatedItemsSearcher.connectHitsView2(relatedItemsAdapter, product, matchingPatterns) { response ->
                response.hits.deserialize(Product.serializer())
            }
            relatedItemsSearcher.searchAsync()
        }

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        relatedItemsSearcher.cancel()
        connection.clear()
    }
}
