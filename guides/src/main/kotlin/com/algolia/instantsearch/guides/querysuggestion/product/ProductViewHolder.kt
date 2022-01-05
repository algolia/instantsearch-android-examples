package com.algolia.instantsearch.guides.querysuggestion.product

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.helper.android.highlighting.toSpannedString
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_large.view.*

class ProductViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: Product) {
        view.itemTitle.text = item.highlightedName?.toSpannedString() ?: item.name
        view.itemSubtitle.text = item.salePrice
        Glide
            .with(view.context)
            .load(item.thumbnailImage)
            .into(view.itemImage)
    }
}
