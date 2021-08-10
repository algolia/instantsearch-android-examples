package com.algolia.instantsearch.showcase.compose.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.PopupProperties
import com.algolia.instantsearch.compose.searchbox.SearchBoxState

@Composable
fun AutoCompleteText(
    modifier: Modifier = Modifier,
    searchBoxState: SearchBoxState,
    label: @Composable (() -> Unit)? = null,
    suggestions: List<String> = emptyList(),
) {
    Column(modifier = modifier) {
        val isExpanded = remember { mutableStateOf(searchBoxState.query.isNotEmpty()) }
        OutlinedTextField(
            value = searchBoxState.query,
            onValueChange = { query ->
                isExpanded.value = query.isNotEmpty()
                searchBoxState.query = query
                searchBoxState.changeValue(query, false)
            },
            modifier = Modifier.fillMaxWidth(),
            label = label,
            singleLine = true
        )
        DropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = isExpanded.value,
            onDismissRequest = { isExpanded.value = false },
            properties = PopupProperties(focusable = false)
        ) {
            suggestions.forEach { suggestion ->
                DropdownMenuItem(
                    content = { Text(suggestion) },
                    onClick = {
                        isExpanded.value = false
                        searchBoxState.query = suggestion
                        searchBoxState.changeValue(suggestion, true)
                    },
                )
            }
        }
    }
}
