package com.algolia.instantsearch.insights.showcase

import androidx.recyclerview.widget.DiffUtil

object ListItemCallback : DiffUtil.ItemCallback<ItemModel>() {

    override fun areItemsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
        return oldItem.listItem.objectId == newItem.listItem.objectId
    }

    override fun areContentsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
        return oldItem.listItem == newItem.listItem
    }
}
