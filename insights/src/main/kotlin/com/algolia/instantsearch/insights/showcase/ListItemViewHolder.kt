package com.algolia.instantsearch.insights.showcase

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.helper.tracker.HitsTracker
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.list_item_large.view.*

class ListItemViewHolder(itemView: View, private val hitsTracker: HitsTracker) :
    RecyclerView.ViewHolder(itemView) {

    private val title = itemView.itemTitle
    private val substitle = itemView.itemSubtitle
    private val imageView = itemView.itemImage
    private val buy = itemView.buy

    fun bind(model: ItemModel) {
        val listItem = model.listItem
        title.text = listItem.name
        substitle.text = listItem.shortDescription.orEmpty()
        buy.setOnClickListener { hitsTracker.trackConvert(listItem) }
        itemView.setOnClickListener {
             hitsTracker.trackClick(listItem, model.position)
        }

        Glide
            .with(itemView.context)
            .load(listItem.image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }
}