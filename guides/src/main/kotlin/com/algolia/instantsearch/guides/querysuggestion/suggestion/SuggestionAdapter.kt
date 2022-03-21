package com.algolia.instantsearch.guides.querysuggestion.suggestion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.guides.databinding.ListItemSuggestionBinding

class SuggestionAdapter : ListAdapter<Suggestion, SuggestionViewHolder>(SuggestionAdapter),
    HitsView<Suggestion> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        return SuggestionViewHolder(
            ListItemSuggestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun setHits(hits: List<Suggestion>) {
        submitList(hits)
    }

    companion object : DiffUtil.ItemCallback<Suggestion>() {

        override fun areItemsTheSame(oldItem: Suggestion, newItem: Suggestion): Boolean {
            return oldItem.objectID == newItem.objectID
        }

        override fun areContentsTheSame(oldItem: Suggestion, newItem: Suggestion): Boolean {
            return oldItem == newItem
        }
    }
}