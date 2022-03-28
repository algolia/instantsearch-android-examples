package com.algolia.instantsearch.guides.insights

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.android.inflate
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.guides.R
import com.algolia.instantsearch.guides.insights.ListItemAdapter.ListItemViewHolder
import com.algolia.instantsearch.tracker.HitsTracker
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class ListItemAdapter(private val hitsTracker: HitsTracker) :
    ListAdapter<ListItem, ListItemViewHolder>(ListItemCallback),
    HitsView<ListItem> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        return ListItemViewHolder(hitsTracker, parent.inflate(R.layout.list_item_large_buy))
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, position) }
    }

    override fun setHits(hits: List<ListItem>) {
        submitList(hits)
    }

    class ListItemViewHolder(private val hitsTracker: HitsTracker, val view: View) :
        RecyclerView.ViewHolder(view) {

        fun bind(model: ListItem, position: Int) {
            view.findViewById<TextView>(R.id.itemTitle).text = model.name
            view.findViewById<TextView>(R.id.itemSubtitle).text = model.description.orEmpty()
            view.findViewById<Button>(R.id.buy).setOnClickListener { hitsTracker.trackConvert(model) }
            // The first object in the list of search results has a position of 1 (not zero)
            itemView.setOnClickListener { hitsTracker.trackClick(model, position + 1) }

            Glide
                .with(view.context)
                .load(model.image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(view.findViewById(R.id.itemImage))
        }
    }

    object ListItemCallback : DiffUtil.ItemCallback<ListItem>() {
        override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem) = oldItem.objectID == newItem.objectID
        override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem) = oldItem == newItem
    }
}
