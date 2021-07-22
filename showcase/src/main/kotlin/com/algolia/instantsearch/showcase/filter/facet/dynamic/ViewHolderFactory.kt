package com.algolia.instantsearch.showcase.filter.facet.dynamic

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.algolia.instantsearch.helper.android.filter.facet.dynamic.DynamicFacetListHeaderViewHolder
import com.algolia.instantsearch.helper.android.filter.facet.dynamic.DynamicFacetListItemViewHolder
import com.algolia.instantsearch.helper.android.filter.facet.dynamic.DynamicFacetListViewHolder
import com.algolia.instantsearch.helper.android.filter.facet.dynamic.DynamicFacetListViewHolder.ViewType
import com.algolia.instantsearch.helper.android.filter.facet.dynamic.DynamicFacetModel
import com.algolia.instantsearch.helper.android.inflate
import com.algolia.instantsearch.showcase.R
import kotlinx.android.synthetic.main.list_item_selectable.view.*

class ViewHolderFactory : DynamicFacetListViewHolder.Factory {

    override fun createViewHolder(
        parent: ViewGroup,
        viewType: ViewType
    ): DynamicFacetListViewHolder<out DynamicFacetModel> {
        return when (viewType) {
            ViewType.Header -> HeaderViewHolder(parent.inflate<TextView>(R.layout.header_item))
            ViewType.Item -> ItemViewHolder(parent.inflate(R.layout.list_item_selectable))
        }
    }

    class HeaderViewHolder(view: TextView) : DynamicFacetListHeaderViewHolder(view) {
        override fun bind(item: DynamicFacetModel.Header, onClick: View.OnClickListener?) {
            val textView = view as TextView
            textView.text = item.attribute.raw
        }
    }

    class ItemViewHolder(view: View) : DynamicFacetListItemViewHolder(view) {
        override fun bind(item: DynamicFacetModel.Item, onClick: View.OnClickListener?) {
            view.selectableItemName.text = item.facet.value
            view.selectableItemSubtitle.text = "${item.facet.count}"
            view.selectableItemSubtitle.visibility = View.VISIBLE
            view.selectableItemIcon.visibility = if (item.selected) View.VISIBLE else View.GONE
            view.setOnClickListener(onClick)
        }
    }
}
