package com.algolia.instantsearch.guides.querysuggestion

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.android.list.autoScrollToStart
import com.algolia.instantsearch.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.guides.databinding.ActivityQuerySuggestionBinding
import com.algolia.instantsearch.guides.querysuggestion.product.Product
import com.algolia.instantsearch.guides.querysuggestion.product.ProductAdapter
import com.algolia.instantsearch.guides.querysuggestion.suggestion.Suggestion
import com.algolia.instantsearch.guides.querysuggestion.suggestion.SuggestionAdapter
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.searcher.hits.addHitsSearcher
import com.algolia.instantsearch.searcher.multi.MultiSearcher
import com.algolia.search.client.ClientSearch
import com.algolia.search.helper.deserialize
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import io.ktor.client.features.logging.*

class QuerySuggestionGuide : AppCompatActivity() {

    private val client = ClientSearch(
        applicationID = ApplicationID("latency"),
        apiKey = APIKey("afc3dd66dd1293e2e2736a5a51b05c0a"),
        logLevel = LogLevel.ALL
    )
    private val multiSearcher = MultiSearcher(client)
    private val productSearcher = multiSearcher.addHitsSearcher(
        indexName = IndexName("instant_search")
    )
    private val suggestionSearcher = multiSearcher.addHitsSearcher(
        indexName = IndexName("instantsearch_query_suggestions"),
    )
    private val searchBox = SearchBoxConnector(multiSearcher)
    private val connection = ConnectionHandler(searchBox)

    private lateinit var binding: ActivityQuerySuggestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuerySuggestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup search box
        val searchBoxView = SearchBoxViewAppCompat(binding.searchView)
        connection += searchBox.connectView(searchBoxView)

        // Setup hits
        val productAdapter = ProductAdapter()
        binding.products.configureRecyclerView(productAdapter)
        connection += productSearcher.connectHitsView(productAdapter) { it.hits.deserialize(Product.serializer()) }

        // Setup suggestions
        val suggestionAdapter = SuggestionAdapter {
            searchBoxView.setText(it.query, true)
        }
        binding.suggestions.configureRecyclerView(suggestionAdapter)
        connection += suggestionSearcher.connectHitsView(suggestionAdapter) {
            it.hits.deserialize(Suggestion.serializer())
        }

        // Switch display depending search box focus
        searchBoxView.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) display(Display.Suggestions) else display(Display.Hits)
        }

        // initial search
        multiSearcher.searchAsync()
    }

    private fun RecyclerView.configureRecyclerView(recyclerViewAdapter: RecyclerView.Adapter<*>) {
        visibility = View.VISIBLE
        layoutManager = LinearLayoutManager(this@QuerySuggestionGuide)
        adapter = recyclerViewAdapter
        itemAnimator = null
        autoScrollToStart(recyclerViewAdapter)
    }

    override fun onDestroy() {
        super.onDestroy()
        multiSearcher.cancel()
        connection.clear()
    }

    private fun display(display: Display) {
        binding.suggestionsGroup.visibility = display.visibleIf(Display.Suggestions)
        binding.hitsGroup.visibility = display.visibleIf(Display.Hits)
    }

    private enum class Display {
        Suggestions, Hits;

        fun visibleIf(display: Display) = if (this == display) View.VISIBLE else View.GONE
    }
}
