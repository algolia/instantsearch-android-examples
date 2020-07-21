package com.algolia.instantsearch.showcase.hierarchical

import android.graphics.Typeface
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.helper.hierarchical.HierarchicalItem
import com.algolia.instantsearch.showcase.dip
import kotlinx.android.synthetic.main.list_item_selectable.view.*

class HierarchicalViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: HierarchicalItem, onClick: View.OnClickListener) {
        view.elevation = ((6 - item.level * 2) * view.context.dip(4)).toFloat()
        view.selectableItemName.text = item.displayName
        view.selectableItemSubtitle.text = item.facet.count.toString()
        view.selectableItemSubtitle.visibility = View.VISIBLE
        view.setOnClickListener(onClick)
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
