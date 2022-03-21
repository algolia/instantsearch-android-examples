package com.algolia.instantsearch.guides.querysuggestion.product

import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.guides.databinding.ListItemLargeBinding
import com.algolia.instantsearch.android.highlighting.toSpannedString
import com.bumptech.glide.Glide

class ProductViewHolder(private val binding: ListItemLargeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Product) {
        binding.itemTitle.text = item.highlightedName?.toSpannedString() ?: item.name
        binding.itemSubtitle.text = item.salePrice
        Glide
            .with(binding.root.context)
            .load(item.thumbnailImage)
            .into(binding.itemImage)
    }
}
