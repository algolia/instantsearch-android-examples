package com.algolia.instantsearch.showcase.filter.facet.dynamic

sealed class FacetItem {
    data class Header(val raw: String) : FacetItem()
    data class Value(val raw: String) : FacetItem()
}
