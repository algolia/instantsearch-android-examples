package com.algolia.instantsearch.showcase.filter.facet.dynamic

import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet

sealed class FacetItem {
    data class Header(val attribute: Attribute) : FacetItem()
    data class Value(val attribute: Attribute, val facet: Facet, val selected: Boolean) : FacetItem()
}
