package com.algolia.instantsearch.showcase.compose.directory

import com.algolia.instantsearch.showcase.compose.filter.clear.ShowcaseFilterClear
import com.algolia.instantsearch.showcase.compose.filter.current.ShowcaseFilterCurrent
import com.algolia.search.model.ObjectID

val showcases = mapOf(
    ObjectID("filter_current") to ShowcaseFilterCurrent::class,
    ObjectID("filter_clear") to ShowcaseFilterClear::class,
    ObjectID("filter_toggle") to ShowcaseFilterCurrent::class,
    ObjectID("filter_segment") to ShowcaseFilterCurrent::class,
    ObjectID("filter_list_all") to ShowcaseFilterCurrent::class,
    ObjectID("filter_list_numeric") to ShowcaseFilterCurrent::class,
    ObjectID("filter_list_facet") to ShowcaseFilterCurrent::class,
    ObjectID("filter_list_tag") to ShowcaseFilterCurrent::class,
    ObjectID("facet_list") to ShowcaseFilterCurrent::class,
    ObjectID("facet_list_persistent") to ShowcaseFilterCurrent::class,
    ObjectID("facet_list_search") to ShowcaseFilterCurrent::class,
    ObjectID("filter_numeric_comparison") to ShowcaseFilterCurrent::class,
    ObjectID("filter_numeric_range") to ShowcaseFilterCurrent::class,
    ObjectID("search_on_submit") to ShowcaseFilterCurrent::class,
    ObjectID("search_as_you_type") to ShowcaseFilterCurrent::class,
    ObjectID("search_auto_complete_text_view") to ShowcaseFilterCurrent::class,
    ObjectID("paging_single_searcher") to ShowcaseFilterCurrent::class,
    ObjectID("sort_by") to ShowcaseFilterCurrent::class,
    ObjectID("stats") to ShowcaseFilterCurrent::class,
    ObjectID("loading") to ShowcaseFilterCurrent::class,
    ObjectID("paging_single_index") to ShowcaseFilterCurrent::class,
    ObjectID("paging_multiple_index") to ShowcaseFilterCurrent::class,
    ObjectID("filter_hierarchical") to ShowcaseFilterCurrent::class,
    ObjectID("merged_list") to ShowcaseFilterCurrent::class,
    ObjectID("highlighting") to ShowcaseFilterCurrent::class,
    ObjectID("personalisation_related_items") to ShowcaseFilterCurrent::class,
    ObjectID("query_rule_custom_data") to ShowcaseFilterCurrent::class,
    ObjectID("filter_rating") to ShowcaseFilterCurrent::class,
)
