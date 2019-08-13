package com.algolia.instantsearch.showcase.highlighting

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.showcase.list.movie.Movie
import com.algolia.instantsearch.showcase.list.movie.MovieDiffUtil
import com.algolia.instantsearch.helper.android.inflate


class HighlightingAdapter : ListAdapter<Movie, HighlightingViewHolder>(MovieDiffUtil), HitsView<Movie> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighlightingViewHolder {
        return HighlightingViewHolder(parent.inflate(R.layout.list_item_highlighting))
    }

    override fun onBindViewHolder(holder: HighlightingViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun setHits(hits: List<Movie>) {
        submitList(hits)
    }
}