package com.algolia.instantsearch.showcase.compose.directory

import com.algolia.instantsearch.showcase.compose.filter.clear.FilterClearShowcase
import com.algolia.instantsearch.showcase.compose.filter.current.FilterCurrentShowcase
import com.algolia.instantsearch.showcase.compose.filter.facet.FacetListSearchShowcase
import com.algolia.instantsearch.showcase.compose.filter.facet.FacetListShowcase
import com.algolia.search.model.ObjectID

val showcases = mapOf(
    ObjectID("filter_current") to FilterCurrentShowcase::class,
    ObjectID("filter_clear") to FilterClearShowcase::class,
    ObjectID("facet_list") to FacetListShowcase::class,
    ObjectID("facet_list_search") to FacetListSearchShowcase::class,
)
