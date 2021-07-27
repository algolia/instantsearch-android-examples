package com.algolia.instantsearch.showcase.compose.directory

import com.algolia.instantsearch.showcase.compose.filter.clear.FilterClearShowcase
import com.algolia.instantsearch.showcase.compose.filter.current.FilterCurrentShowcase
import com.algolia.instantsearch.showcase.compose.filter.facet.FacetListShowcase
import com.algolia.search.model.ObjectID

val showcases = mapOf(
    ObjectID("filter_current") to FilterCurrentShowcase::class,
    ObjectID("filter_clear") to FilterClearShowcase::class,
    ObjectID("filter_toggle") to FilterCurrentShowcase::class,
    ObjectID("filter_segment") to FilterCurrentShowcase::class,
    ObjectID("filter_list_all") to FilterCurrentShowcase::class,
    ObjectID("filter_list_numeric") to FilterCurrentShowcase::class,
    ObjectID("filter_list_facet") to FilterCurrentShowcase::class,
    ObjectID("filter_list_tag") to FilterCurrentShowcase::class,
    ObjectID("facet_list") to FacetListShowcase::class,
    ObjectID("facet_list_persistent") to FilterCurrentShowcase::class,
    ObjectID("facet_list_search") to FilterCurrentShowcase::class,
    ObjectID("filter_numeric_comparison") to FilterCurrentShowcase::class,
    ObjectID("filter_numeric_range") to FilterCurrentShowcase::class,
    ObjectID("search_on_submit") to FilterCurrentShowcase::class,
    ObjectID("search_as_you_type") to FilterCurrentShowcase::class,
    ObjectID("search_auto_complete_text_view") to FilterCurrentShowcase::class,
    ObjectID("paging_single_searcher") to FilterCurrentShowcase::class,
    ObjectID("sort_by") to FilterCurrentShowcase::class,
    ObjectID("stats") to FilterCurrentShowcase::class,
    ObjectID("loading") to FilterCurrentShowcase::class,
    ObjectID("paging_single_index") to FilterCurrentShowcase::class,
    ObjectID("paging_multiple_index") to FilterCurrentShowcase::class,
    ObjectID("filter_hierarchical") to FilterCurrentShowcase::class,
    ObjectID("merged_list") to FilterCurrentShowcase::class,
    ObjectID("highlighting") to FilterCurrentShowcase::class,
    ObjectID("personalisation_related_items") to FilterCurrentShowcase::class,
    ObjectID("query_rule_custom_data") to FilterCurrentShowcase::class,
    ObjectID("filter_rating") to FilterCurrentShowcase::class,
)
