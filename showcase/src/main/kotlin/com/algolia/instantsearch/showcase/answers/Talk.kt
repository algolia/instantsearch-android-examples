package com.algolia.instantsearch.showcase.answers

import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Talk(
    @SerialName("objectID") override val objectID: ObjectID,
    @SerialName("title") val title: String,
    @SerialName("main_speaker") val mainSpeaker: String,
    @SerialName("transcript") val transcript: String
) : Indexable {

    @Transient
    var answer: String? = null
}
