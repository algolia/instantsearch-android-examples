package com.algolia.instantsearch.showcase.relateditems.api.internal.extensions

// TODO(): Should be done by passing a param for example to FilterGroupsConverter.Legacy.Facet.
internal fun List<List<String>>.unquote(): List<List<String>> {
    return map { innerList ->
        innerList.map { it.replace("\"", "") }
    }
}
