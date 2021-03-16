package com.algolia.instantsearch.insights

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.helper.android.inflate
import com.algolia.instantsearch.helper.tracker.HitsTracker

class ListItemAdapter(private val hitsTracker: HitsTracker) :
    ListAdapter<ItemModel, ListItemViewHolder>(ListItemCallback),
    HitsView<ItemModel> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        return ListItemViewHolder(parent.inflate(R.layout.list_item_large), hitsTracker)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun setHits(hits: List<ItemModel>) {
        submitList(hits)
    }
}