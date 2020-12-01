package com.algolia.instantsearch.showcase.answers

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.loading.connectView
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.loading.LoadingConnector
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.SearchMode.OnSubmit
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.SearcherAnswers
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.showcase.configureRecyclerView
import com.algolia.instantsearch.showcase.configureSearchView
import com.algolia.instantsearch.showcase.configureToolbar
import com.algolia.instantsearch.showcase.databinding.IncludeAnswerBinding
import com.algolia.search.ExperimentalAlgoliaClientAPI
import com.algolia.search.client.ClientSearch
import com.algolia.search.configuration.ConfigurationSearch
import com.algolia.search.helper.deserialize
import com.algolia.search.helper.toAttribute
import com.algolia.search.helper.toIndexName
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.AnswersQuery
import com.algolia.search.model.search.Language
import io.ktor.client.features.logging.LogLevel
import kotlinx.android.synthetic.main.include_search.*
import kotlinx.android.synthetic.main.showcase_answers.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalAlgoliaClientAPI::class)
class AnswersShowcase : AppCompatActivity() {

    // Searchers
    private val answersClient = ClientSearch(
        ConfigurationSearch(
            ApplicationID("CKOEQ4XGMU"),
            APIKey("f811d6a70c2b7b82d1622891a2d2e6de"),
            logLevel = LogLevel.ALL
        )
    )
    private val tedIndex = answersClient.initIndex("ted".toIndexName())
    private val indexSearcher = SearcherSingleIndex(tedIndex)
    private val answersSearcher = SearcherAnswers(
        index = tedIndex,
        request = AnswersQuery(
            query = "",
            queryLanguages = listOf(Language.English),
            attributesForPrediction = listOf(
                "description".toAttribute(),
                "title".toAttribute(),
                "transcript".toAttribute()
            )
        )
    )

    // Search Boxes
    private val searchBoxViewModel = SearchBoxViewModel()
    private val answersSearchBox = SearchBoxConnector(
        searcher = answersSearcher,
        searchMode = OnSubmit,
        viewModel = searchBoxViewModel
    )
    private val searchBox = SearchBoxConnector(
        searcher = indexSearcher,
        searchMode = OnSubmit,
        viewModel = searchBoxViewModel
    )

    // Loading
    private val loadingViewModel: LoadingViewModel = LoadingViewModel()
    private val searchLoading = LoadingConnector(indexSearcher, loadingViewModel)
    private val answersLoading = LoadingConnector(answersSearcher, loadingViewModel)

    // Connections
    private val connection = ConnectionHandler(
        searchLoading, answersLoading, searchBox, answersSearchBox
    )

    // Hits Adapters
    private val adapter = AnswerAdapter()
    private val searchAdapterConnection = connectSearch(adapter)
    private val answersAdapterConnection = connectAnswers(adapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showcase_answers)
        val searchBoxView = SearchBoxViewAppCompat(searchView)
        connection += searchBox.connectView(searchBoxView) // or answersSearchBox.connectView

        val progressView = ProgressBarView(progressBar)
        connection += loadingViewModel.connectView(progressView)

        val answerView = AnswerView(IncludeAnswerBinding.bind(answer))
        val answerViewConnection = connectAnswers(answerView)

        val switcher = ConnectionSwitcher(
            searchAdapterConnection, answersAdapterConnection, answerViewConnection
        )
        dropdown
            .searchModeFlow()
            .distinctUntilChanged()
            .onEach { searchMode -> selectMode(searchMode, switcher) }
            .launchIn(lifecycleScope)

        configureSearchView(searchView, getString(R.string.search_answers))
        configureToolbar(toolbar)
        configureRecyclerView(hits, adapter)
        configureDropdown(dropdown)

        connection.connections.addAll(
            listOf(searchAdapterConnection, answersAdapterConnection, answerViewConnection)
        )
    }

    private fun selectMode(searchMode: SearchMode, switcher: ConnectionSwitcher) {
        answer.visibility = View.GONE
        switcher.switchTo(searchMode)
        indexSearcher.searchAsync()
        answersSearcher.searchAsync()
    }

    private fun connectSearch(view: HitsView<Talk>) =
        indexSearcher.connectHitsView(view) { response ->
            response.hits.deserialize(Talk.serializer())
        }

    private fun connectAnswers(view: HitsView<Talk>) =
        answersSearcher.connectHitsView(view) { response ->
            deserializeTalks(response)
        }

    private fun deserializeTalks(response: ResponseSearch): List<Talk> {
        return response.hits.map { hit ->
            hit.deserialize(Talk.serializer()).apply {
                val answer = hit.answer
                if (answer.extractAttribute != Attribute("title")) {
                    this.answer = answer.extract
                }
            }
        }
    }

    private fun configureDropdown(autoCompleteTextView: AutoCompleteTextView) {
        val adapter = ArrayAdapter(
            this,
            R.layout.dropdown_menu_popup_item,
            SearchMode.asStringList(this)
        )
        autoCompleteTextView.setAdapter(adapter)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun AutoCompleteTextView.searchModeFlow(): Flow<SearchMode> {
        return callbackFlow {
            val listener = AdapterView.OnItemClickListener { _, _, position, _ ->
                sendBlocking(SearchMode.values()[position])
            }
            onItemClickListener = listener
            awaitClose { onItemClickListener = null }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        indexSearcher.cancel()
        answersSearcher.cancel()
        connection.clear()
    }
}
