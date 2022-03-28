package com.algolia.instantsearch.guides.gettingstarted

import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.guides.databinding.ListItemSmallBinding
import com.algolia.instantsearch.android.highlighting.toSpannedString
import com.algolia.instantsearch.guides.model.Product


class ProductViewHolder(private val binding: ListItemSmallBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(product: Product) {
        binding.itemName.text = product.highlightedName?.toSpannedString() ?: product.name
    }
}
