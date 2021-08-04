package com.algolia.instantsearch.showcase.compose.directory

import com.algolia.instantsearch.showcase.compose.filter.clear.FilterClearShowcase
import com.algolia.instantsearch.showcase.compose.filter.current.FilterCurrentShowcase
import com.algolia.instantsearch.showcase.compose.filter.facet.FacetListPersistentShowcase
import com.algolia.instantsearch.showcase.compose.filter.facet.FacetListSearchShowcase
import com.algolia.instantsearch.showcase.compose.filter.facet.FacetListShowcase
import com.algolia.instantsearch.showcase.compose.filter.list.FilterListAllShowcase
import com.algolia.instantsearch.showcase.compose.filter.list.FilterListFacetShowcase
import com.algolia.search.model.ObjectID

val showcases = mapOf(
    ObjectID("filter_current") to FilterCurrentShowcase::class,
    ObjectID("filter_clear") to FilterClearShowcase::class,
    ObjectID("facet_list") to FacetListShowcase::class,
    ObjectID("facet_list_search") to FacetListSearchShowcase::class,
    ObjectID("facet_list_persistent") to FacetListPersistentShowcase::class,
    ObjectID("filter_list_all") to FilterListAllShowcase::class,
    ObjectID("filter_list_facet") to FilterListFacetShowcase::class,
)
