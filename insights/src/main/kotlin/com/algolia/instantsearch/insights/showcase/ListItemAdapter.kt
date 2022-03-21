package com.algolia.instantsearch.insights.showcase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.tracker.HitsTracker
import com.algolia.instantsearch.insights.showcase.databinding.ListItemLargeBinding

class ListItemAdapter(private val hitsTracker: HitsTracker) :
    ListAdapter<ItemModel, ListItemViewHolder>(ListItemCallback),
    HitsView<ItemModel> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        return ListItemViewHolder(
            hitsTracker,
            ListItemLargeBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun setHits(hits: List<ItemModel>) {
        submitList(hits)
    }
}
