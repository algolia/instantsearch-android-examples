package com.algolia.instantsearch.showcase.list.movie

import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.text.buildSpannedString
import androidx.core.text.italic
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.helper.android.highlighting.toSpannedString
import com.algolia.instantsearch.helper.android.inflate
import com.algolia.instantsearch.showcase.R
import com.algolia.search.model.ObjectID
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_large.view.itemImage
import kotlinx.android.synthetic.main.list_item_large.view.itemSubtitle
import kotlinx.android.synthetic.main.list_item_large.view.itemTitle
import kotlinx.android.synthetic.main.list_item_large_favorite.view.*

class FavoriteMovieAdapterPaged :
    PagedListAdapter<Movie, FavoriteMovieAdapterPaged.ViewHolder>(MovieDiffUtil) {

    private val selectedItems = mutableSetOf<ObjectID>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.list_item_large_favorite))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null) holder.bind(item)
    }

    inner class ViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {

        fun bind(movie: Movie) {
            view.itemTitle.text =
                TextUtils.concat(movie.highlightedTitle?.toSpannedString(), " (${movie.year})")
            view.itemSubtitle.text = movie.highlightedGenres?.toSpannedString()
                ?: buildSpannedString { italic { append("unknown genre") } }
            Glide.with(view)
                .load(movie.image).placeholder(android.R.drawable.ic_media_play)
                .centerCrop()
                .into(view.itemImage)

            view.favorite.isFavorite(movie)
            view.favorite.setOnClickListener { _ ->
                val id = movie.objectID
                if (selectedItems.contains(id)) selectedItems.remove(id) else selectedItems.add(id)
                view.favorite.isFavorite(movie)
            }
        }

        private fun ImageView.isFavorite(movie: Movie) {
            val isFav = selectedItems.contains(movie.objectID)
            val favoriteImg = if (isFav) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            setImageResource(favoriteImg)
        }
    }
}
