package com.algolia.instantsearch.guides.gettingstarted

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.guides.databinding.FragmentProductBinding
import com.algolia.instantsearch.android.list.autoScrollToStart
import com.algolia.instantsearch.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.android.stats.StatsTextView
import com.algolia.instantsearch.searchbox.connectView
import com.algolia.instantsearch.stats.StatsPresenterImpl
import com.algolia.instantsearch.stats.connectView

class ProductFragment : Fragment() {

    private val connection = ConnectionHandler()

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity())[MyViewModel::class.java]

        val adapterProduct = ProductAdapter()

        viewModel.products.observe(viewLifecycleOwner) { pagingData ->
            adapterProduct.submitData(lifecycle, pagingData)
        }

        binding.productList.let {
            it.itemAnimator = null
            it.adapter = adapterProduct
            it.layoutManager = LinearLayoutManager(requireContext())
            it.autoScrollToStart(adapterProduct)
        }

        val searchBoxView = SearchBoxViewAppCompat(binding.searchView)
        connection += viewModel.searchBox.connectView(searchBoxView)

        val statsView = StatsTextView(binding.stats)
        connection += viewModel.stats.connectView(statsView, StatsPresenterImpl())

        binding.filters.setOnClickListener { (requireActivity() as GettingStartedGuide).showFacetFragment() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        connection.clear()
    }
}