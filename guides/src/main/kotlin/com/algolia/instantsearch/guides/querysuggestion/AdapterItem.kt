package com.algolia.instantsearch.guides.querysuggestion

import android.view.View
import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.instantsearch.helper.highlighting.Highlightable
import com.algolia.search.model.Attribute
import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonObject


sealed class AdapterItem : Indexable {

    @Serializable
    data class Product(
        val name: String,
        val thumbnailImage: String,
        val salePrice: String,
        override val objectID: ObjectID,
        override val _highlightResult: JsonObject?
    ) : Highlightable, AdapterItem() {

        @Transient
        public val highlightedName: HighlightedString?
            get() = getHighlight(Attribute("name"))
    }

    @Serializable
    data class Suggestion(
        val query: String,
        override val objectID: ObjectID,
        override val _highlightResult: JsonObject?
    ) : AdapterItem(), Highlightable {

        @Transient
        public val highlightedQuery: HighlightedString?
            get() = getHighlight(Attribute("query"))

        var onClickListener: View.OnClickListener? = null
    }

    data class Header(
        val text: String,
        override val objectID: ObjectID = ObjectID(text)
    ) : AdapterItem()
}