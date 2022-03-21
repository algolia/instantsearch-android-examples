package com.algolia.instantsearch.guides.gettingstarted

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.algolia.instantsearch.guides.databinding.ListItemSelectableBinding
import com.algolia.instantsearch.android.filter.facet.FacetListViewHolder
import com.algolia.search.model.search.Facet

class MyFacetListViewHolder(private val binding: ListItemSelectableBinding) :
    FacetListViewHolder(binding.root) {

    override fun bind(facet: Facet, selected: Boolean, onClickListener: View.OnClickListener) {
        binding.root.setOnClickListener(onClickListener)
        binding.facetCount.text = facet.count.toString()
        binding.facetCount.visibility = View.VISIBLE
        binding.icon.visibility = if (selected) View.VISIBLE else View.INVISIBLE
        binding.facetName.text = facet.value
    }

    object Factory : FacetListViewHolder.Factory {

        override fun createViewHolder(parent: ViewGroup): FacetListViewHolder {
            return MyFacetListViewHolder(
                ListItemSelectableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }
}
