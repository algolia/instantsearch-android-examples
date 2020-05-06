package com.algolia.instantsearch.showcase.relateditems.api

import com.algolia.search.model.Attribute
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1

data class MatchingPattern<T>(
    val attribute: Attribute,
    val score: Int,
    val property: KProperty1<T, *>
)
