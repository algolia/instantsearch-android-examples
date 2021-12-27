package com.algolia.exchange.query.suggestions.hits

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NorthWest
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import coil.compose.rememberImagePainter
import com.algolia.instantsearch.compose.highlighting.toAnnotatedString
import com.algolia.instantsearch.compose.hits.HitsState
import com.algolia.instantsearch.compose.searchbox.SearchBox
import com.algolia.instantsearch.compose.searchbox.SearchBoxState

@Composable
fun QuerySuggestion(
    modifier: Modifier = Modifier,
    searchBoxState: SearchBoxState,
    suggestionsState: HitsState<Suggestion>,
    hitsState: HitsState<Product>,
) {


    Column(modifier.fillMaxWidth()) {
        SearchBoxSuggestions(
            searchBoxState = searchBoxState,
            suggestionsState = suggestionsState
        )

        LazyColumn {
            items(hitsState.hits) {
                ProductRow(it)
            }
        }
    }
}

@Composable
private fun SearchBoxSuggestions(
    modifier: Modifier = Modifier,
    searchBoxState: SearchBoxState,
    suggestionsState: HitsState<Suggestion>
) {
    var expanded by remember { mutableStateOf(false) }
    var dropDownWidth by remember { mutableStateOf(0) }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        SearchBox(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { dropDownWidth = it.width },
            searchBoxState = searchBoxState,
            elevation = 4.dp,
            onValueChange = { value, _ ->
                expanded = value.isNotEmpty() && suggestionsState.hits.isNotEmpty()
            }
        )
        DropdownMenu(
            modifier = Modifier
                .width(with(LocalDensity.current) { dropDownWidth.toDp() })
                .heightIn(max = TextFieldDefaults.MinHeight * 6),
            expanded = expanded,
            onDismissRequest = { expanded = false },
            properties = PopupProperties(focusable = false)
        ) {
            suggestionsState.hits.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        searchBoxState.setText(selectionOption.query, true)
                        expanded = false
                    }
                ) {
                    Row {
                        val text = selectionOption.highlightedQuery?.toAnnotatedString()
                            ?: AnnotatedString(selectionOption.query)
                        Text(modifier = Modifier.weight(1f), text = text)
                        Icon(
                            imageVector = Icons.Default.NorthWest,
                            tint = MaterialTheme.colors.onBackground.copy(alpha = 0.2f),
                            contentDescription = "query suggestion"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductRow(product: Product) {
    Row(Modifier.padding(12.dp)) {
        Image(
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Crop,
            painter = rememberImagePainter(
                data = product.image,
                builder = {
                    placeholder(android.R.drawable.ic_menu_report_image)
                    error(android.R.drawable.ic_menu_report_image)
                },
            ),
            contentDescription = product.name,
        )

        Column(Modifier.padding(start = 8.dp)) {
            Text(
                text = product.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.subtitle2
            )
            Text(
                text = product.description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.caption,
            )
        }
    }
}
