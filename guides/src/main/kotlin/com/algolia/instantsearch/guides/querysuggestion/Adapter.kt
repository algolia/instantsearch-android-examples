package com.algolia.instantsearch.guides.querysuggestion

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.guides.R
import com.algolia.instantsearch.helper.android.inflate


class Adapter :
    ListAdapter<AdapterItem, ViewHolder>(Adapter),
    HitsView<AdapterItem> {

    enum class Type {
        Product,
        Suggestion,
        Header
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (Type.values()[viewType]) {
            Type.Product -> ViewHolder.Product(parent.inflate(R.layout.list_item_image, false))
            Type.Suggestion -> ViewHolder.Suggestion(parent.inflate(R.layout.list_item_suggestion, false))
            Type.Header -> ViewHolder.Header(parent.inflate<TextView>(R.layout.list_item_header, false))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is AdapterItem.Product -> Type.Product
            is AdapterItem.Suggestion -> Type.Suggestion
            is AdapterItem.Header -> Type.Header
        }.ordinal
    }

    override fun setHits(hits: List<AdapterItem>) {
        submitList(hits)
    }

    companion object : DiffUtil.ItemCallback<AdapterItem>() {

        override fun areItemsTheSame(oldItem: AdapterItem, newItem: AdapterItem): Boolean {
            return oldItem.objectID == newItem.objectID
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: AdapterItem, newItem: AdapterItem): Boolean {
            return oldItem == newItem
        }
    }
}