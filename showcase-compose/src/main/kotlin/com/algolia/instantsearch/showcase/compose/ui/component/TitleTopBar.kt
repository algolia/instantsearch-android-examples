package com.algolia.instantsearch.showcase.compose.filter.current.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.algolia.instantsearch.showcase.compose.ui.ShowcaseTheme

@Preview
@Composable
fun TitleTopBarPreview() {
    ShowcaseTheme {
        TitleTopBar(title = "Filters")
    }
}

@Composable
fun TitleTopBar(modifier: Modifier = Modifier, title: String, onBackPressed: () -> Unit = {}) {
    TopAppBar(
        modifier = modifier,
        title = { Text(title) },
        backgroundColor = MaterialTheme.colors.background,
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable(
                        onClick = onBackPressed,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    )
                    .padding(start = 16.dp)
            )
        },
    )
}
