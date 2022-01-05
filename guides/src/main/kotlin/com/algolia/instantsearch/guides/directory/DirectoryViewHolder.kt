package com.algolia.instantsearch.guides.directory

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.guides.databinding.ListItemHeaderBinding
import com.algolia.instantsearch.guides.databinding.ListItemSmallBinding
import com.algolia.instantsearch.helper.android.highlighting.toSpannedString
import com.algolia.search.serialize.KeyName

sealed class DirectoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    data class Header(val binding: ListItemHeaderBinding) : DirectoryViewHolder(binding.root) {

        fun bind(item: DirectoryItem.Header) {
            binding.root.text = item.name
        }
    }

    data class Item(val binding: ListItemSmallBinding) : DirectoryViewHolder(binding.root) {

        fun bind(item: DirectoryItem.Item) {
            val view = binding.root
            val text = item.hit.highlightedName?.toSpannedString() ?: item.hit.name
            binding.itemName.text = text

            view.setOnClickListener {
                val intent = Intent(view.context, guides.getValue(item.hit.objectID).java).apply {
                    putExtra(KeyName, item.hit.name)
                }

                view.context.startActivity(intent)
            }
        }
    }
}