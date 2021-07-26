package com.algolia.instantsearch.showcase.compose

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.algolia.instantsearch.compose.searchbox.SearchQuery
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.showcase.compose.ui.*
import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.FilterGroup
import com.algolia.search.model.filter.FilterGroupsConverter
import com.algolia.search.serialize.KeyIndexName
import io.ktor.client.features.logging.*

val client = ClientSearch(
    ConfigurationSearch(
        ApplicationID("latency"),
        APIKey("1f6fd3a6fb973cb08419fe7d288fa4db"),
        logLevel = LogLevel.ALL
    )
)

val stubIndex = client.initIndex(IndexName("stub"))

fun AppCompatActivity.configureSearcher(searcher: SearcherSingleIndex) {
    searcher.index = client.initIndex(intent.indexName)
}

fun AppCompatActivity.configureSearcher(searcher: SearcherForFacets) {
    searcher.index = client.initIndex(intent.indexName)
}

val Intent.indexName: IndexName get() = IndexName(extras!!.getString(KeyIndexName)!!)

fun <R> configureSearchBox(
    searcher: Searcher<R>,
    searcherQuery: SearchQuery,
    connections: ConnectionHandler
) {
    SearchBoxConnector(searcher).also {
        connections += it
        connections += it.connectView(searcherQuery)
    }
}

fun Set<FilterGroup<*>>.highlight(
    converter: FilterGroupsConverter<Set<FilterGroup<*>>, String?> = FilterGroupsConverter.SQL.Unquoted,
    colors: Map<String, Color> = mapOf(),
    defaultColor: Color = Color.Black
): AnnotatedString {
    return with(AnnotatedString.Builder()) {
        forEachIndexed { index, group ->
            val color = colors.getOrElse(group.name ?: "") { defaultColor }
            val string = converter(setOf(group)) ?: ""
            withStyle(SpanStyle(color)) {
                append(string)
            }
            if (index < size - 1) {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(" AND ")
                }
            }
        }
        toAnnotatedString()
    }
}

fun filterColors(vararg attributes: Attribute): Map<String, Color> {
    return attributes.mapIndexed { index, attribute ->
        attribute.raw to when (index) {
            0 -> HoloRedDark
            1 -> HoloBlueDark
            2 -> HoloGreenDark
            3 -> HoloPurple
            else -> Black
        }
    }.toMap()
}
