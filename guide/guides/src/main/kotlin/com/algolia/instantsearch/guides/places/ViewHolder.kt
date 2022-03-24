package com.algolia.instantsearch.guides.places

import android.text.SpannedString
import android.text.TextUtils
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.core.highlighting.HighlightTokenizer
import com.algolia.instantsearch.guides.databinding.ListItemSmallBinding
import com.algolia.instantsearch.android.highlighting.toSpannedString
import com.algolia.search.model.places.PlaceLanguage
import com.algolia.search.model.search.HighlightResult
import com.algolia.search.serialize.toHighlights

class ViewHolder(private val binding: ListItemSmallBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(place: PlaceLanguage) {
        val name = place.highlightResultOrNull
            ?.toHighlights("locale_names")
            ?.firstOrNull()
            ?.tokenize() ?: place.localNames.first()
        val county = place.highlightResultOrNull
            ?.toHighlights("county")
            ?.firstOrNull()
            ?.tokenize() ?: place.county.first()
        val postCode = place.postCodeOrNull?.firstOrNull()?.let { ", $it" } ?: ""

        binding.itemName.text = TextUtils.concat(name, ", ", county, postCode)
    }

    private fun HighlightResult.tokenize(): SpannedString {
        return HighlightTokenizer()(value).toSpannedString()
    }
}
