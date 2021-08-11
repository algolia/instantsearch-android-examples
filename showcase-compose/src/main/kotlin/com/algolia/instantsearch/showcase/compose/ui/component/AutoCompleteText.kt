package com.algolia.instantsearch.showcase.compose.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.PopupProperties
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.compose.sortby.SortByState

@Composable
fun DropdownList(
    modifier: Modifier = Modifier,
    sortByState: SortByState,
    label: @Composable (() -> Unit)? = null
) {
    var isExpanded by remember { mutableStateOf(false) }
    return AutoCompleteText(
        modifier = modifier,
        label = label,
        readOnly = true,
        dropDownElements = sortByState.options,
        isExpanded = isExpanded,
        text = sortByState.options[sortByState.selected] ?: "",
        onDropdownItemSelect = { index, _ ->
            isExpanded = false
            sortByState.onSelectionChange?.invoke(index)
        },
        onDropdownMenuDismiss = {
            //isExpanded = false
        },
        onDropdownIconClick = {
            isExpanded = !isExpanded
        }
    )
}

@Composable
fun AutoCompleteText(
    modifier: Modifier = Modifier,
    searchBoxState: SearchBoxState,
    label: @Composable (() -> Unit)? = null,
    suggestions: List<String> = emptyList(),
) {
    var isExpanded by remember { mutableStateOf(searchBoxState.query.isNotEmpty()) }
    return AutoCompleteText(
        modifier = modifier,
        label = label,
        dropDownElements = suggestions.toDropdownElements(),
        isExpanded = isExpanded,
        text = searchBoxState.query,
        onValueChange = { query ->
            isExpanded = query.isNotEmpty()
            searchBoxState.query = query
            searchBoxState.changeValue(query, false)
        },
        onDropdownItemSelect = { _, query ->
            isExpanded = false
            searchBoxState.query = query
            searchBoxState.changeValue(query, false)
        },
        onDropdownMenuDismiss = {
            isExpanded = false
        }
    )
}

@Composable
fun AutoCompleteText(
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
    text: String = "",
    onValueChange: (String) -> Unit = {},
    label: @Composable (() -> Unit)? = null,
    onDropdownMenuDismiss: () -> Unit = {},
    onDropdownItemSelect: (Int, String) -> Unit = { _, _ -> },
    onDropdownIconClick: () -> Unit = {},
    dropDownElements: Map<Int, String> = emptyMap(),
    readOnly: Boolean = false
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = onValueChange,
            label = label,
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = onDropdownIconClick) {
                    DropdownIcon(isExpanded)
                }
            },
            readOnly = readOnly
        )

        DropdownMenu(
            modifier = Modifier.wrapContentSize(Alignment.TopStart),
            expanded = isExpanded,
            onDismissRequest = onDropdownMenuDismiss,
            properties = PopupProperties(focusable = false)
        ) {
            dropDownElements.forEach { (index, suggestion) ->
                DropdownMenuItem(
                    content = {
                        Text(
                            text = suggestion,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    onClick = { onDropdownItemSelect(index, suggestion) },
                )
            }
        }
    }
}

@Composable
fun DropdownIcon(isExpanded: Boolean) {
    val iconResource = if (isExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown
    Icon(iconResource, null)
}

private fun List<String>.toDropdownElements(): Map<Int, String> {
    var i = 0
    return map { i++ to it }.toMap()
}
