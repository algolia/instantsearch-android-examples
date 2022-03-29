package com.algolia.instantsearch.guides.gettingstarted

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.android.stats.StatsTextView
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.guides.R
import com.algolia.instantsearch.guides.extension.configure
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.stats.StatsPresenterImpl
import com.algolia.instantsearch.stats.connectView

class ProductFragment : Fragment(R.layout.fragment_product) {

    private val connection = ConnectionHandler()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity())[MyViewModel::class.java]

        val adapterProduct = ProductAdapter()

        viewModel.products.observe(viewLifecycleOwner) { pagingData ->
            adapterProduct.submitData(lifecycle, pagingData)
        }

        view.findViewById<RecyclerView>(R.id.productList).configure(adapterProduct)

        val searchBoxView = SearchBoxViewAppCompat(view.findViewById(R.id.searchView))
        connection += viewModel.searchBox.connectView(searchBoxView)

        val statsView = StatsTextView(view.findViewById(R.id.stats))
        connection += viewModel.stats.connectView(statsView, StatsPresenterImpl())

        view.findViewById<Button>(R.id.filters)
            .setOnClickListener { (requireActivity() as GettingStartedGuide).showFacetFragment() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        connection.clear()
    }
}