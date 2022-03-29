package com.algolia.instantsearch.guides.gettingstarted

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.android.filter.facet.FacetListAdapter
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.filter.facet.connectView
import com.algolia.instantsearch.guides.R
import com.algolia.instantsearch.guides.extension.configure

class FacetFragment : Fragment(R.layout.fragment_facet) {

    private val connection = ConnectionHandler()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity())[MyViewModel::class.java]

        val adapterFacet = FacetListAdapter(MyFacetListViewHolder.Factory)
        val facetList = view.findViewById<RecyclerView>(R.id.facetList)

        (requireActivity() as AppCompatActivity).let {
            it.setSupportActionBar(view.findViewById(R.id.toolbar))
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        connection += viewModel.facetList.connectView(adapterFacet, viewModel.facetPresenter)

        facetList.configure(adapterFacet)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        connection.clear()
    }
}
