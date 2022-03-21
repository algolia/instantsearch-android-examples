package com.algolia.instantsearch.guides.querysuggestion.suggestion

import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.guides.databinding.ListItemSuggestionBinding
import com.algolia.instantsearch.android.highlighting.toSpannedString

class SuggestionViewHolder(private val binding: ListItemSuggestionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Suggestion) {
        binding.root.setOnClickListener(item.onClickListener)
        binding.itemName.text = item.highlightedQuery?.toSpannedString() ?: item.query
    }
}
