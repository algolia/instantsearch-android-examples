package com.algolia.instantsearch.guides.querysuggestion.product

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.android.highlighting.toSpannedString
import com.algolia.instantsearch.android.inflate
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.guides.R
import com.algolia.instantsearch.guides.extension.ProductDiffUtil
import com.algolia.instantsearch.guides.model.Product
import com.algolia.instantsearch.guides.querysuggestion.product.ProductAdapter.ProductViewHolder
import com.bumptech.glide.Glide

class ProductAdapter : ListAdapter<Product, ProductViewHolder>(ProductDiffUtil), HitsView<Product> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ProductViewHolder(parent.inflate(R.layout.list_item_large))

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(getItem(position))

    override fun setHits(hits: List<Product>) = submitList(hits)

    class ProductViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: Product) {
            view.findViewById<TextView>(R.id.itemTitle).text =
                item.highlightedName?.toSpannedString() ?: item.name
            view.findViewById<TextView>(R.id.itemSubtitle).text = item.price.toString()
            Glide
                .with(view.context)
                .load(item.image)
                .into(view.findViewById(R.id.itemImage))
        }
    }
}
