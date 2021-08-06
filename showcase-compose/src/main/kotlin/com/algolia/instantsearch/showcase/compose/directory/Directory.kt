package com.algolia.instantsearch.showcase.compose.directory

import com.algolia.instantsearch.showcase.compose.filter.clear.FilterClearShowcase
import com.algolia.instantsearch.showcase.compose.filter.current.FilterCurrentShowcase
import com.algolia.instantsearch.showcase.compose.filter.facet.FacetListPersistentShowcase
import com.algolia.instantsearch.showcase.compose.filter.facet.FacetListSearchShowcase
import com.algolia.instantsearch.showcase.compose.filter.facet.FacetListShowcase
import com.algolia.instantsearch.showcase.compose.filter.list.FilterListAllShowcase
import com.algolia.instantsearch.showcase.compose.filter.list.FilterListFacetShowcase
import com.algolia.instantsearch.showcase.compose.filter.list.FilterListNumericShowcase
import com.algolia.instantsearch.showcase.compose.filter.list.FilterListTagShowcase
import com.algolia.instantsearch.showcase.compose.filter.map.FilterMapShowcase
import com.algolia.instantsearch.showcase.compose.filter.numeric.comparison.FilterComparisonShowcase
import com.algolia.instantsearch.showcase.compose.filter.range.FilterRangeShowcase
import com.algolia.instantsearch.showcase.compose.filter.rating.RatingShowcase
import com.algolia.instantsearch.showcase.compose.filter.toggle.FilterToggleShowcase
import com.algolia.instantsearch.showcase.compose.hierarchical.HierarchicalShowcase
import com.algolia.instantsearch.showcase.compose.highlighting.HighlightingShowcase
import com.algolia.instantsearch.showcase.compose.list.merged.MergedListShowcase
import com.algolia.search.model.ObjectID

val showcases = mapOf(
    ObjectID("filter_current") to FilterCurrentShowcase::class,
    ObjectID("filter_clear") to FilterClearShowcase::class,
    ObjectID("facet_list") to FacetListShowcase::class,
    ObjectID("facet_list_search") to FacetListSearchShowcase::class,
    ObjectID("facet_list_persistent") to FacetListPersistentShowcase::class,
    ObjectID("filter_list_all") to FilterListAllShowcase::class,
    ObjectID("filter_list_facet") to FilterListFacetShowcase::class,
    ObjectID("filter_list_numeric") to FilterListNumericShowcase::class,
    ObjectID("filter_list_tag") to FilterListTagShowcase::class,
    ObjectID("filter_segment") to FilterMapShowcase::class,
    ObjectID("filter_numeric_comparison") to FilterComparisonShowcase::class,
    ObjectID("filter_numeric_range") to FilterRangeShowcase::class,
    ObjectID("filter_rating") to RatingShowcase::class,
    ObjectID("filter_toggle") to FilterToggleShowcase::class,
    ObjectID("filter_hierarchical") to HierarchicalShowcase::class,
    ObjectID("highlighting") to HighlightingShowcase::class,
    ObjectID("merged_list") to MergedListShowcase::class,
)
