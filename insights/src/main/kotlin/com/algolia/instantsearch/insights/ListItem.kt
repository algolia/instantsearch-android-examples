package com.algolia.instantsearch.insights

import kotlinx.serialization.Serializable

@Serializable
data class ListItem(
    val name: String,
    val image: String,
    val queryId: String,
    val objectId: String,
    val position: Int
)
