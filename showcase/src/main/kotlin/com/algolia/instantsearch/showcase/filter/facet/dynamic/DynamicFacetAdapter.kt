package com.algolia.instantsearch.showcase.filter.facet.dynamic

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.helper.android.inflate
import com.algolia.instantsearch.helper.filter.facet.dynamic.DynamicFacetView
import com.algolia.instantsearch.helper.filter.facet.dynamic.FacetSelections
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.showcase.directory.DirectoryAdapter
import com.algolia.instantsearch.showcase.directory.DirectoryItem
import com.algolia.instantsearch.showcase.filter.facet.dynamic.DynamicFacetAdapter.DynamicFacetViewHolder
import com.algolia.search.model.Attribute
import com.algolia.search.model.rule.AttributedFacets
import com.algolia.search.model.search.Facet
import kotlinx.android.synthetic.main.list_item_small.view.*

class DynamicFacetAdapter : ListAdapter<FacetItem, DynamicFacetViewHolder>(diffUtil),
    DynamicFacetView {

    private enum class ViewType {
        Header,
        Item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DynamicFacetViewHolder {
        return when (ViewType.values()[viewType]) {
            ViewType.Header -> DynamicFacetViewHolder.Header(parent.inflate<TextView>(R.layout.header_item))
            ViewType.Item -> DynamicFacetViewHolder.Item(parent.inflate(R.layout.list_item_small))
        }
    }

    override fun onBindViewHolder(holder: DynamicFacetViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is DynamicFacetViewHolder.Header -> holder.bind(item as FacetItem.Header)
            is DynamicFacetViewHolder.Item -> holder.bind(item as FacetItem.Value)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item) {
            is FacetItem.Header -> ViewType.Header
            is FacetItem.Value -> ViewType.Item
        }.ordinal
    }

    override var didSelect: ((Attribute, Facet) -> Unit)? = null

    override fun commit(selections: FacetSelections) {
        //val list = mutableListOf<FacetItem>()
        //selections.onEach { (attribute, values) ->
        //    list += FacetItem.Header(attribute.raw)
        //    values.onEach {
        //        list += FacetItem.Value(it)
        //    }
        //}
        //submitList(list)
        Log.d("DynamicFacetAdapter", "$selections")
    }

    override fun commit(facetOrder: List<AttributedFacets>) {
        val list = mutableListOf<FacetItem>()
        facetOrder.onEach { attributedFacets ->
            list += FacetItem.Header(attributedFacets.attribute.raw)
            attributedFacets.facets.onEach {
                list += FacetItem.Value(it.value)
            }
        }
        submitList(list)
    }

    sealed class DynamicFacetViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        data class Header(val view: TextView) : DynamicFacetViewHolder(view) {
            fun bind(item: FacetItem.Header) {
                view.text = item.raw
            }
        }

        data class Item(val view: View) : DynamicFacetViewHolder(view) {
            fun bind(item: FacetItem.Value) {
                view.itemName.text = item.raw
            }
        }
    }

    companion object {

        private val diffUtil = object : DiffUtil.ItemCallback<FacetItem>() {
            override fun areItemsTheSame(oldItem: FacetItem, newItem: FacetItem): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: FacetItem, newItem: FacetItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
