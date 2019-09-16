package com.algolia.instantsearch.guides.gettingstarted

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.helper.android.highlighting.toSpannedString
import kotlinx.android.synthetic.main.list_item_small.view.*


class ProductViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(product: Product) {
        view.itemName.text = product.highlightedName?.toSpannedString() ?: product.name
    }
}