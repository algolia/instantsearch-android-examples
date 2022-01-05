package com.algolia.instantsearch.guides.gettingstarted

import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.guides.databinding.ListItemSmallBinding
import com.algolia.instantsearch.helper.android.highlighting.toSpannedString


class ProductViewHolder(private val binding: ListItemSmallBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(product: Product) {
        binding.itemName.text = product.highlightedName?.toSpannedString() ?: product.name
    }
}
