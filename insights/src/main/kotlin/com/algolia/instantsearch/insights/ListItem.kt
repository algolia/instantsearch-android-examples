package com.algolia.instantsearch.insights

import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import kotlinx.serialization.Serializable

@Serializable
data class ListItem(
    override val objectID: ObjectID,
    val name: String,
    val image: String,
    val queryId: String? = null,
    val objectId: String? = null,
    val position: Int? = null,
) : Indexable
