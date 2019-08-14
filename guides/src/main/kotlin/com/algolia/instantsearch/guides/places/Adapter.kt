package com.algolia.instantsearch.guides.places

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.guides.R
import com.algolia.instantsearch.helper.android.inflate
import com.algolia.search.model.places.PlaceLanguage


class Adapter : ListAdapter<PlaceLanguage, ViewHolder>(Adapter), HitsView<PlaceLanguage> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.list_item_text))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null) holder.bind(item)
    }

    override fun setHits(hits: List<PlaceLanguage>) {
        submitList(hits)
    }

    companion object : DiffUtil.ItemCallback<PlaceLanguage>() {

        override fun areItemsTheSame(oldItem: PlaceLanguage, newItem: PlaceLanguage): Boolean {
            return oldItem::class == newItem::class
        }

        override fun areContentsTheSame(oldItem: PlaceLanguage, newItem: PlaceLanguage): Boolean {
            return oldItem == newItem
        }
    }
}