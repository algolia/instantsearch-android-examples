package com.algolia.exchange.querysuggestions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.algolia.exchange.querysuggestions.ui.theme.SearchAppTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchAppTheme {
                QuerySuggestion(
                    searchBoxState = viewModel.searchBoxState,
                    hitsState = viewModel.suggestionsState,
                )
            }
        }
    }
}
