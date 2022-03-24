package com.algolia.instantsearch.guides.querysuggestion.suggestion

import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.android.highlighting.toSpannedString
import com.algolia.instantsearch.guides.databinding.ListItemSuggestionBinding

class SuggestionViewHolder(private val binding: ListItemSuggestionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Suggestion, onClick: ((Suggestion) -> Unit)? = null) {
        binding.root.setOnClickListener { onClick?.invoke(item) }
        binding.itemName.text = item.highlightedQuery?.toSpannedString() ?: item.query
    }
}
