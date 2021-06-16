package com.algolia.instantsearch.showcase.filter.facet.dynamic

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.helper.android.inflate
import com.algolia.instantsearch.helper.filter.facet.dynamic.AttributedFacets
import com.algolia.instantsearch.helper.filter.facet.dynamic.DynamicFacetView
import com.algolia.instantsearch.helper.filter.facet.dynamic.SelectionsPerAttribute
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.showcase.filter.facet.dynamic.DynamicFacetAdapter.DynamicFacetViewHolder
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet
import kotlinx.android.synthetic.main.list_item_selectable.view.*

class DynamicFacetAdapter : ListAdapter<FacetItem, DynamicFacetViewHolder>(DiffUtil), DynamicFacetView {

    var facetSelections: SelectionsPerAttribute = emptyMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DynamicFacetViewHolder {
        return when (ViewType.values()[viewType]) {
            ViewType.Header -> DynamicFacetViewHolder.Header(parent.inflate<TextView>(R.layout.header_item))
            ViewType.Item -> DynamicFacetViewHolder.Item(parent.inflate(R.layout.list_item_selectable))
        }
    }

    override fun onBindViewHolder(holder: DynamicFacetViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is DynamicFacetViewHolder.Header -> holder.bind(item as FacetItem.Header)
            is DynamicFacetViewHolder.Item -> holder.bind(item as FacetItem.Value, item.selected) {
                didSelect?.let { it(item.attribute, item.facet) }
            }
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

    override fun setSelections(selections: SelectionsPerAttribute) {
        if (facetSelections == selections) return
        facetSelections = selections
        updateListItems()
    }

    private fun updateListItems() {
        val newList = currentList.map { facetItem ->
            when (facetItem) {
                is FacetItem.Header -> facetItem
                is FacetItem.Value -> facetItem.copy(selected = isSelected(facetItem.attribute, facetItem.facet))
            }
        }
        if (currentList != newList) submitList(newList)
    }

    override fun setOrderedFacets(facetOrder: List<AttributedFacets>) {
        val list = mutableListOf<FacetItem>()
        facetOrder.onEach { (attribute, facets) ->
            list += FacetItem.Header(attribute)
            facets.onEach { facet ->
                val isSelected = isSelected(attribute, facet)
                list += FacetItem.Value(attribute, facet, isSelected)
            }
        }
        submitList(list)
    }

    private fun isSelected(attribute: Attribute, facet: Facet) = facetSelections[attribute]?.contains(facet.value) == true

    enum class ViewType {
        Header,
        Item
    }

    sealed class DynamicFacetViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        data class Header(val view: TextView) : DynamicFacetViewHolder(view) {
            fun bind(item: FacetItem.Header) {
                view.text = item.attribute.raw
            }
        }

        data class Item(val view: View) : DynamicFacetViewHolder(view) {
            init {
                view.selectableItemSubtitle.visibility = View.VISIBLE
            }

            fun bind(item: FacetItem.Value, selected: Boolean, onClick: () -> Unit) {
                view.selectableItemName.text = item.facet.value
                view.selectableItemSubtitle.text = "(${item.facet.count})"
                view.selectableItemIcon.visibility = if (selected) View.VISIBLE else View.GONE
                view.setOnClickListener { onClick() }
            }
        }
    }

    companion object {

        private val DiffUtil = object : DiffUtil.ItemCallback<FacetItem>() {
            override fun areItemsTheSame(oldItem: FacetItem, newItem: FacetItem): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: FacetItem, newItem: FacetItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
