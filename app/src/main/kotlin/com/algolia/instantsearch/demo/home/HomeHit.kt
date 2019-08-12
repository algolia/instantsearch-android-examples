package com.algolia.instantsearch.demo.home

import com.algolia.instantsearch.helper.highlighting.Highlightable
import com.algolia.search.model.Attribute
import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import com.algolia.search.model.search.HighlightResult
import com.algolia.search.serialize.Key_HighlightResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonObject


@Serializable
data class HomeHit(
    override val objectID: ObjectID,
    val name: String,
    val type: String,
    val index: String,
    override val _highlightResult: JsonObject? = null
): Indexable, Highlightable {

    @Transient
    public val highlightedName
        get() = getHighlight(Attribute("name"), preTag = "<b>", postTag = "</b>")
}