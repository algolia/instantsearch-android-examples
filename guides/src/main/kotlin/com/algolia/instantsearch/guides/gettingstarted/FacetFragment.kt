package com.algolia.instantsearch.guides.gettingstarted

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.guides.databinding.FragmentFacetBinding
import com.algolia.instantsearch.helper.android.filter.facet.FacetListAdapter
import com.algolia.instantsearch.helper.android.list.autoScrollToStart
import com.algolia.instantsearch.helper.filter.facet.connectView

class FacetFragment : Fragment() {

    private val connection = ConnectionHandler()

    private var _binding: FragmentFacetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFacetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity())[MyViewModel::class.java]

        val adapterFacet = FacetListAdapter(MyFacetListViewHolder.Factory)

        binding.facetList.let {
            it.adapter = adapterFacet
            it.layoutManager = LinearLayoutManager(requireContext())
            it.autoScrollToStart(adapterFacet)
        }
        (requireActivity() as AppCompatActivity).let {
            it.setSupportActionBar(binding.toolbar)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        connection += viewModel.facetList.connectView(adapterFacet, viewModel.facetPresenter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        connection.clear()
    }
}
