package com.algolia.instantsearch.insights

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.helper.tracker.HitsTracker
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.list_item.view.*

class ListItemViewHolder(itemView: View, private val hitsTracker: HitsTracker) :
    RecyclerView.ViewHolder(itemView) {

    private val textView = itemView.textView
    private val imageView = itemView.imageView
    private val button = itemView.button

    fun bind(item: ListItem) {
        textView.text = item.name
        button.setOnClickListener { hitsTracker.trackConvert(item) }
        itemView.setOnClickListener {
            item.position?.let { position -> hitsTracker.trackClick(item, position) }
        }

        Glide
            .with(itemView.context)
            .load(item.image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }
}