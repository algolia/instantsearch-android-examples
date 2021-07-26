package com.algolia.instantsearch.showcase.compose.filter.current

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.toFilterGroups
import com.algolia.search.model.filter.FilterGroup

interface FilterGroupsState {

    val filterGroups: Set<FilterGroup<*>>
}

fun FilterGroupsState(filterState: FilterState): FilterGroupsState {
    return FilterGroupStateImpl(filterState)
}

class FilterGroupStateImpl(filterState: FilterState) : FilterGroupsState {

    override var filterGroups: Set<FilterGroup<*>> by mutableStateOf(emptySet()) // order of this statement and init block matters!

    init {
        filterState.filters.subscribePast {
            filterGroups = it.toFilterGroups()
        }
    }
}
