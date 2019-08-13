package com.algolia.instantsearch.guides.places

import android.text.SpannedString
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.core.highlighting.HighlightTokenizer
import com.algolia.instantsearch.helper.android.highlighting.toSpannedString
import com.algolia.search.model.places.PlaceLanguage
import com.algolia.search.model.search.HighlightResult
import com.algolia.search.serialize.toHighlights
import kotlinx.android.synthetic.main.place_item.view.*


class PlacesViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(place: PlaceLanguage) {
        println(place.highlightResultOrNull)
        val name = place.highlightResultOrNull
            ?.toHighlights("locale_names")
            ?.firstOrNull()
            ?.tokenize() ?: place.localNames.first()
        val county = place.highlightResultOrNull
            ?.toHighlights("county")
            ?.firstOrNull()
            ?.tokenize() ?: place.county.first()
        val postCode = place.postCodeOrNull?.firstOrNull()?.let { ", $it" } ?: ""

        view.placeName.text = TextUtils.concat(name, ", ", county, postCode)
    }

    fun HighlightResult.tokenize(): SpannedString {
        return HighlightTokenizer()(value).toSpannedString()
    }
}