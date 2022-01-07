package com.algolia.instantsearch.showcase.list.movie

import android.text.TextUtils
import android.view.View
import androidx.core.text.buildSpannedString
import androidx.core.text.italic
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.helper.android.highlighting.toSpannedString
import com.algolia.instantsearch.showcase.databinding.ListItemLargeBinding
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_large.view.*

class MovieViewHolder(private val binding: ListItemLargeBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie) {
        binding.itemTitle.text = TextUtils.concat(movie.highlightedTitle?.toSpannedString(), " (${movie.year})")
        binding.itemSubtitle.text = movie.highlightedGenres?.toSpannedString()
            ?: buildSpannedString { italic { append("unknown genre") } }
        Glide.with(binding.root)
            .load(movie.image).placeholder(android.R.drawable.ic_media_play)
            .centerCrop()
            .into(binding.itemImage)
    }
}
