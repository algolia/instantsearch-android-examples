package com.algolia.instantsearch.insights.showcase

import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.tracker.HitsTracker
import com.algolia.instantsearch.insights.showcase.databinding.ListItemLargeBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class ListItemViewHolder(private val hitsTracker: HitsTracker, binding: ListItemLargeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val title = binding.itemTitle
    private val substitle = binding.itemSubtitle
    private val imageView = binding.itemImage
    private val buy = binding.buy

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
