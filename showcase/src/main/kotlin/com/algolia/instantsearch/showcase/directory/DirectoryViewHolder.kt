package com.algolia.instantsearch.showcase.directory

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.helper.android.highlighting.toSpannedString
import com.algolia.search.serialize.KeyIndexName
import com.algolia.search.serialize.KeyName
import kotlinx.android.synthetic.main.list_item_small.view.*


sealed class DirectoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    data class Header(val view: TextView) : DirectoryViewHolder(view) {

        fun bind(item: DirectoryItem.Header) {
            view.text = item.name
        }
    }

    data class Item(val view: View) : DirectoryViewHolder(view) {

        fun bind(item: DirectoryItem.Item) {
            val text = item.hit.highlightedName?.toSpannedString() ?: item.hit.name

            view.itemName.text = text
            view.setOnClickListener {
                val intent = Intent(view.context, showcases.getValue(item.hit.objectID).java).apply {
                    putExtra(KeyIndexName, item.hit.index)
                    putExtra(KeyName, item.hit.name)
                }

                view.context.startActivity(intent)
            }
        }
    }
}