package com.algolia.instantsearch.showcase.list.merged

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.helper.android.inflate
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.showcase.list.movie.Movie
import com.algolia.instantsearch.showcase.list.movie.MovieViewHolder

class MoviesAdapter : ListAdapter<Movie, MovieViewHolder>(MoviesAdapter), HitsView<Movie> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(parent.inflate(R.layout.list_item_large))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val product = getItem(position)
        if (product != null) holder.bind(product)
    }

    companion object : DiffUtil.ItemCallback<Movie>() {

        override fun areItemsTheSame(
            oldItem: Movie,
            newItem: Movie
        ): Boolean {
            return oldItem.objectID == newItem.objectID
        }

        override fun areContentsTheSame(
            oldItem: Movie,
            newItem: Movie
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun setHits(hits: List<Movie>) {
        submitList(hits)
    }
}
