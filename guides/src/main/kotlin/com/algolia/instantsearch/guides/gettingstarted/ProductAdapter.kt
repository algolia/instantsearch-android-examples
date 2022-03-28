package com.algolia.instantsearch.guides.gettingstarted

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.algolia.instantsearch.guides.databinding.ListItemSmallBinding
import com.algolia.instantsearch.guides.extension.ProductDiffUtil
import com.algolia.instantsearch.guides.model.Product

class ProductAdapter : PagingDataAdapter<Product, ProductViewHolder>(ProductDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ListItemSmallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        if (product != null) holder.bind(product)
    }
}
