package com.algolia.instantsearch.showcase.relateditems

import com.algolia.instantsearch.helper.highlighting.Highlightable
import com.algolia.search.model.Attribute
import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonObject

@Serializable
data class Product(
    val name: String,
    val description: String,
    val brand: String,
    val categories: List<String>,
    val type: String,
    val price: Double,
    @SerialName("price_range") val priceRange: String,
    val image: String,
    val url: String,
    @SerialName("free_shipping") val freeShipping: Boolean,
    val rating: Int,
    val popularity: Long,
    override val objectID: ObjectID,
    override val _highlightResult: JsonObject?
) : Indexable, Highlightable {

    @Transient
    public val highlightedName
        get() = getHighlight(Attribute("name"))

    @Transient
    public val highlightedDescription
        get() = getHighlights(Attribute("description"))

    @Transient
    public val highlightedBrand
        get() = getHighlights(Attribute("brand"))

    @Transient
    public val highlightedCategories
        get() = getHighlights(Attribute("categories"))

    @Transient
    public val highlightedType
        get() = getHighlights(Attribute("type"))
}
