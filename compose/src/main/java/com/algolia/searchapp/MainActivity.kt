package com.algolia.searchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.algolia.searchapp.ui.theme.SearchAppTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchAppTheme {
                Search(
                    searchQuery = viewModel.searchQuery,
                    productPager = viewModel.hitsPaginator,
                    statsText = viewModel.statsText,
                    facetList = viewModel.facetList
                )
            }
        }
    }
}
