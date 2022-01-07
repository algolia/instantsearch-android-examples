package com.algolia.instantsearch.showcase.hierarchical

import android.graphics.Typeface
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.helper.hierarchical.HierarchicalItem
import com.algolia.instantsearch.showcase.databinding.ListItemSelectableBinding
import com.algolia.instantsearch.showcase.dip
import kotlinx.android.synthetic.main.list_item_selectable.view.*

class HierarchicalViewHolder(private val binding: ListItemSelectableBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val view get() = binding.root

    fun bind(item: HierarchicalItem, onClick: View.OnClickListener) {
        view.elevation = ((6 - item.level * 2) * view.context.dip(4)).toFloat()
        view.setOnClickListener(onClick)
        binding.selectableItemName.text = item.displayName
        binding.selectableItemSubtitle.text = item.facet.count.toString()
        binding.selectableItemSubtitle.visibility = View.VISIBLE
        if (item.isSelected) selected() else unselected()
    }

    private fun selected() {
        view.selectableItemIcon.visibility = View.VISIBLE
        view.selectableItemName.setTypeface(null, Typeface.BOLD)
    }

    private fun unselected() {
        view.selectableItemIcon.visibility = View.INVISIBLE
        view.selectableItemName.setTypeface(null, Typeface.NORMAL)
    }
}
