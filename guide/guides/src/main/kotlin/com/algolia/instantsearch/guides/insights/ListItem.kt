package com.algolia.instantsearch.guides.insights

import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import kotlinx.serialization.Serializable

@Serializable
data class ListItem(
    override val objectID: ObjectID,
    val name: String,
    val description: String? = null,
    val image: String,
) : Indexable
