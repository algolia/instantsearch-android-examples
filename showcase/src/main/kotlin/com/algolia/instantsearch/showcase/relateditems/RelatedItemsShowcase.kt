package com.algolia.instantsearch.showcase.relateditems

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.helper.filter.state.filterState
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.showcase.configureRecyclerView
import com.algolia.instantsearch.showcase.configureSearcher
import com.algolia.instantsearch.showcase.configureToolbar
import com.algolia.instantsearch.showcase.stubIndex
import com.algolia.search.helper.deserialize
import com.algolia.search.model.Attribute
import kotlinx.android.synthetic.main.showcase_relateditems.hits
import kotlinx.android.synthetic.main.showcase_relateditems.relatedItems
import kotlinx.android.synthetic.main.showcase_relateditems.toolbar
import kotlinx.coroutines.launch

class RelatedItemsShowcase : AppCompatActivity() {

    private val searcher = SearcherSingleIndex(stubIndex)
    private val relatedItemsSearcher = SearcherSingleIndex(stubIndex)
    private val connection = ConnectionHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showcase_relateditems)
        searcher.query.hitsPerPage = 3 // Limit to 3 results

        configureSearcher(searcher)
        configureSearcher(relatedItemsSearcher)
        configureToolbar(toolbar)

        val hitsAdapter = ProductAdapter()
        connection += searcher.connectHitsView(hitsAdapter) { response ->
            response.hits.deserialize(Product.serializer())
        }
        configureRecyclerView(hits, hitsAdapter)

        val relatedItemsAdapter = ProductAdapter()
        configureRecyclerView(relatedItems, relatedItemsAdapter)

        hitsAdapter.callback = { product ->

            searcher.coroutineScope.launch {
                relatedItemsSearcher.query.apply {
                    sumOrFiltersScores = true
                    facetFilters = listOf(listOf("objectID:-${product.objectID}"))
                    optionalFilters = listOf(
                        listOf("brand:${product.brand}<score=1>"),
                        product.categories.map { "categories:$it<score=2>" })
                }

                val response = relatedItemsSearcher.search()
                val hits = response.hits.deserialize(Product.serializer())
                relatedItemsAdapter.setHits(hits)
            }
        }

        searcher.searchAsync()
    }

    fun generateOptionalFilters(
        product: Product,
        matchingPatterns: List<MatchingPattern<Product>>
    ): List<List<String>>? {
        MatchingPattern(Attribute("brand"), 1, Product::brand)
        MatchingPattern(Attribute("brand"), 1, Product::categories)

        filterState {
            matchingPatterns.forEach { pattern ->
                when (pattern.property.get(product)) {
                    is Collection<*> -> and {

                    }
                    else -> ""
                }
            }
        }

        return TODO()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        relatedItemsSearcher.cancel()
        connection.clear()
    }
}
