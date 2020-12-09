package com.algolia.instantsearch.showcase.relateditems

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.helper.relateditems.MatchingPattern
import com.algolia.instantsearch.helper.relateditems.connectRelatedHitsView
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.showcase.configureRecyclerView
import com.algolia.instantsearch.showcase.configureSearcher
import com.algolia.instantsearch.showcase.configureToolbar
import com.algolia.instantsearch.showcase.list.product.Product
import com.algolia.instantsearch.showcase.list.product.ProductAdapter
import com.algolia.instantsearch.showcase.stubIndex
import com.algolia.search.helper.deserialize
import com.algolia.search.model.Attribute
import kotlinx.android.synthetic.main.showcase_relateditems.*

class RelatedItemsShowcase : AppCompatActivity() {

    private val searcher = SearcherSingleIndex(stubIndex)
    private val relatedItemsSearcher = SearcherSingleIndex(stubIndex)
    private val connection = ConnectionHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showcase_relateditems)
        searcher.request.hitsPerPage = 3 // Limit to 3 results

        configureToolbar(toolbar)
        configureSearcher(searcher)
        configureSearcher(relatedItemsSearcher)

        val hitsAdapter = ProductAdapter()
        configureRecyclerView(hits, hitsAdapter)
        connection += searcher.connectHitsView(hitsAdapter) { response ->
            response.hits.deserialize(Product.serializer())
        }

        val relatedItemsAdapter = ProductAdapter()
        configureRecyclerView(relatedItems, relatedItemsAdapter)
        val matchingPatterns: List<MatchingPattern<Product>> = listOf(
            MatchingPattern(Attribute("brand"), 1, Product::brand),
            MatchingPattern(Attribute("categories"), 2, Product::categories)
        )
        hitsAdapter.callback = { product ->
            connection += relatedItemsSearcher.connectRelatedHitsView(relatedItemsAdapter, product, matchingPatterns) { response ->
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
