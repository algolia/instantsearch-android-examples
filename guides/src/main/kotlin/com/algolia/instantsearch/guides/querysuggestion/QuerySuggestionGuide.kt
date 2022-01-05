package com.algolia.instantsearch.guides.querysuggestion

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.guides.databinding.ActivityQuerySuggestionBinding
import com.algolia.instantsearch.guides.querysuggestion.product.Product
import com.algolia.instantsearch.guides.querysuggestion.product.ProductAdapter
import com.algolia.instantsearch.guides.querysuggestion.suggestion.Suggestion
import com.algolia.instantsearch.guides.querysuggestion.suggestion.SuggestionAdapter
import com.algolia.instantsearch.helper.android.list.autoScrollToStart
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.hits.addHitsSearcher
import com.algolia.instantsearch.helper.searcher.multi.MultiSearcher
import com.algolia.search.client.ClientSearch
import com.algolia.search.helper.deserialize
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query
import io.ktor.client.features.logging.*

class QuerySuggestionGuide : AppCompatActivity() {

    val client = ClientSearch(
        applicationID = ApplicationID("latency"),
        apiKey = APIKey("1f6fd3a6fb973cb08419fe7d288fa4db"),
        logLevel = LogLevel.ALL
    )
    val multiSearcher = MultiSearcher(client)
    val suggestionSearcher = multiSearcher.addHitsSearcher(
        indexName = IndexName("query_suggestions"),
        query = Query(hitsPerPage = 3)
    )
    val productSearcher = multiSearcher.addHitsSearcher(
        indexName = IndexName("bestbuy_promo"),
        query = Query(hitsPerPage = 3)
    )
    val searchBox = SearchBoxConnector(multiSearcher)
    val connection = ConnectionHandler(searchBox)

    private lateinit var binding: ActivityQuerySuggestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuerySuggestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val searchBoxView = SearchBoxViewAppCompat(binding.searchView)
        val productAdapter = ProductAdapter()
        val suggestionAdapter = SuggestionAdapter()
        configureRecyclerView(binding.suggestions, suggestionAdapter)
        configureRecyclerView(binding.products, productAdapter)

        connection += searchBox.connectView(searchBoxView)
        connection += suggestionSearcher.connectHitsView(suggestionAdapter) {
            it.hits.deserialize(
                Suggestion.serializer()
            )
        }
        connection += productSearcher.connectHitsView(productAdapter) { it.hits.deserialize(Product.serializer()) }

        multiSearcher.searchAsync()
    }

    private fun configureRecyclerView(
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<*>
    ) {
        recyclerView.let {
            it.visibility = View.VISIBLE
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
            it.itemAnimator = null
            it.autoScrollToStart(adapter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        multiSearcher.cancel()
        connection.clear()
    }
}
