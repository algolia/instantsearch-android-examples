package com.algolia.instantsearch.guides.querysuggestion.suggestion

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.helper.android.highlighting.toSpannedString
import kotlinx.android.synthetic.main.list_item_small.view.*

class SuggestionViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: Suggestion) {
        view.setOnClickListener(item.onClickListener)
        view.itemName.text = item.highlightedQuery?.toSpannedString() ?: item.query
    }
}
