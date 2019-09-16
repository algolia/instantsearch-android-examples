package com.algolia.instantsearch.guides.gettingstarted

import android.view.View
import android.view.ViewGroup
import com.algolia.instantsearch.guides.R
import com.algolia.instantsearch.helper.android.filter.facet.FacetListViewHolder
import com.algolia.instantsearch.helper.android.inflate
import com.algolia.search.model.search.Facet
import kotlinx.android.synthetic.main.list_item_selectable.view.*


class FacetViewHolderImpl(view: View) : FacetListViewHolder(view) {

    override fun bind(facet: Facet, selected: Boolean, onClickListener: View.OnClickListener) {
        view.setOnClickListener(onClickListener)
        view.facetCount.text = facet.count.toString()
        view.facetCount.visibility = View.VISIBLE
        view.icon.visibility = if (selected) View.VISIBLE else View.INVISIBLE
        view.facetName.text = facet.value
    }

    object Factory : FacetListViewHolder.Factory {

        override fun createViewHolder(parent: ViewGroup): FacetListViewHolder {
            return FacetViewHolderImpl(parent.inflate(R.layout.list_item_selectable))
        }
    }
}