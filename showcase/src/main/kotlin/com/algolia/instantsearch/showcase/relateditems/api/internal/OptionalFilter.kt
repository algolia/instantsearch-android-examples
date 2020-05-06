package com.algolia.instantsearch.showcase.relateditems.api.internal

import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.showcase.relateditems.api.MatchingPattern
import com.algolia.instantsearch.showcase.relateditems.api.internal.extensions.addMatchingPattern
import com.algolia.instantsearch.showcase.relateditems.api.internal.extensions.toFilterFacetGroup
import com.algolia.instantsearch.showcase.relateditems.api.internal.extensions.unquote
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.FilterGroupsConverter

internal class OptionalFilter(
    val filterGroupID: FilterGroupID,
    val filterFacets: Array<Filter.Facet>
)

internal fun <T> MatchingPattern<T>.toOptionalFilters(hit: T): List<List<String>>? {
    val filterState = FilterState()
    filterState.addMatchingPattern(hit, this)
    return FilterGroupsConverter.Legacy.Facet(filterState.toFilterFacetGroup()).unquote()
}

internal fun <T> List<MatchingPattern<T>>.toOptionalFilters(hit: T): List<List<String>>? {
    val filterState = FilterState()
    forEach { filterState.addMatchingPattern(hit, it) }
    return FilterGroupsConverter.Legacy.Facet(filterState.toFilterFacetGroup()).unquote()
}
