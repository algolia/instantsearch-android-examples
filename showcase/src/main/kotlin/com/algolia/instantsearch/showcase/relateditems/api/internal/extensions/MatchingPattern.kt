package com.algolia.instantsearch.showcase.relateditems.api.internal.extensions

import com.algolia.instantsearch.helper.filter.state.groupAnd
import com.algolia.instantsearch.helper.filter.state.groupOr
import com.algolia.instantsearch.showcase.relateditems.api.MatchingPattern
import com.algolia.instantsearch.showcase.relateditems.api.internal.OptionalFilter
import com.algolia.search.model.filter.Filter

/**
 * Create an [OptionalFilter] from a [MatchingPattern].
 */
internal fun <T> MatchingPattern<T>.toOptionalFilter(hit: T): OptionalFilter {
    return when (val property = property.get(hit)) {
        is Iterable<*> -> {
            val groupOr = groupOr()
            val list = property.map { value -> Filter.Facet(attribute, value.toString(), score) }.toTypedArray()
            OptionalFilter(
                groupOr,
                list
            )
        }
        else -> OptionalFilter(
            groupAnd(),
            arrayOf(Filter.Facet(attribute, property.toString(), score))
        )
    }
}
