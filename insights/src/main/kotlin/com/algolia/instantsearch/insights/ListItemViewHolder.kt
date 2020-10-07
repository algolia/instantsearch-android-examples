package com.algolia.instantsearch.insights

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.search.helper.toObjectID
import com.algolia.search.helper.toQueryID
import com.algolia.search.model.insights.EventName
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.ArrayList

class ListItemViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val textView = itemView.textView
    private val imageView = itemView.imageView
    private val button = itemView.button

    fun bind(item: ListItem) {
        textView.text = item.name
        button.setOnClickListener {
            Insights.shared(App.INDEX_NAME).convertedObjectIDsAfterSearch(
                EventName("conversion"),
                item.queryId.toQueryID(),
                listOf(item.objectId.toObjectID()),
                System.currentTimeMillis()
            )
        }

        itemView.setOnClickListener {
            val positions: MutableList<Int> = ArrayList(1)
            positions.add(item.position)
            Insights.shared(App.INDEX_NAME).clickedObjectIDsAfterSearch(
                EventName("click"),
                item.queryId.toQueryID(),
                listOf(item.objectId.toObjectID()),
                positions,
                System.currentTimeMillis()
            )
        }

        Glide
            .with(itemView.context)
            .load(item.image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }
}