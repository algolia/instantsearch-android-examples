package com.algolia.instantsearch.showcase.relateditems

import com.algolia.search.model.Attribute
import kotlin.reflect.KProperty1

class MatchingPattern<T>(
    val attribute: Attribute,
    val scope: Int,
    val property: KProperty1<T, *>
)

