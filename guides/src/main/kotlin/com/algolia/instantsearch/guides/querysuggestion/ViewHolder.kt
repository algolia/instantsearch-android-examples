package com.algolia.instantsearch.guides.querysuggestion

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.helper.android.highlighting.toSpannedString
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_large.view.*
import kotlinx.android.synthetic.main.list_item_small.view.*


sealed class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    abstract fun bind(item: AdapterItem)

    class Product(view: View) : ViewHolder(view) {

        override fun bind(item: AdapterItem) {
            item as AdapterItem.Product

            view.itemTitle.text = item.highlightedName?.toSpannedString() ?: item.name
            view.itemSubtitle.text = item.salePrice
            Glide
                .with(view.context)
                .load(item.thumbnailImage)
                .into(view.itemImage)
        }
    }

    class Suggestion(view: View) : ViewHolder(view) {

        override fun bind(item: AdapterItem) {
            item as AdapterItem.Suggestion

            view.setOnClickListener(item.onClickListener)
            view.itemName.text = item.highlightedQuery?.toSpannedString() ?: item.query
        }

    }

    class Header(val textView: TextView) : ViewHolder(textView) {

        override fun bind(item: AdapterItem) {
            item as AdapterItem.Header

            textView.text = item.text
        }
    }
}