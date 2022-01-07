package com.algolia.instantsearch.showcase.hierarchical

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.helper.android.inflate
import com.algolia.instantsearch.helper.hierarchical.HierarchicalItem
import com.algolia.instantsearch.helper.hierarchical.HierarchicalView
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.showcase.databinding.ListItemSelectableBinding

class HierarchicalAdapter : ListAdapter<HierarchicalItem, HierarchicalViewHolder>(diffUtil),
    HierarchicalView {

    override var onSelectionChanged: Callback<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HierarchicalViewHolder {
        return HierarchicalViewHolder(
            ListItemSelectableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HierarchicalViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item, View.OnClickListener { onSelectionChanged?.invoke(item.facet.value) })
    }

    override fun setTree(tree: List<HierarchicalItem>) {
        submitList(tree)
    }

    companion object {

        private val diffUtil = object : DiffUtil.ItemCallback<HierarchicalItem>() {

            override fun areItemsTheSame(
                oldItem: HierarchicalItem,
                newItem: HierarchicalItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: HierarchicalItem,
                newItem: HierarchicalItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
