package com.algolia.instantsearch.showcase.relateditems

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.helper.android.highlighting.toSpannedString
import com.algolia.instantsearch.helper.android.inflate
import com.algolia.instantsearch.showcase.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_product.view.itemBrand
import kotlinx.android.synthetic.main.list_item_product.view.itemImage
import kotlinx.android.synthetic.main.list_item_product.view.itemName
import kotlinx.android.synthetic.main.list_item_product.view.itemType

class ProductAdapter : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffUtil), HitsView<Product> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(parent.inflate(R.layout.list_item_product))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) holder.bind(item)
    }

    override fun setHits(hits: List<Product>) {
        submitList(hits)
    }

    class ProductViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(product: Product) {
            view.itemName.text = product.highlightedName?.toSpannedString() ?: product.name
            view.itemBrand.text = product.highlightedBrand?.toSpannedString() ?: product.brand
            view.itemType.text = product.highlightedType?.toSpannedString() ?: product.type
            Glide.with(view)
                .load(product.image).placeholder(android.R.drawable.ic_media_play)
                .centerCrop()
                .into(view.itemImage)
        }
    }

    object ProductDiffUtil : DiffUtil.ItemCallback<Product>() {

        override fun areItemsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem.objectID == newItem.objectID
        }

        override fun areContentsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem == newItem
        }
    }
}
