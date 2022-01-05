package com.algolia.instantsearch.guides.places

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.guides.databinding.ActivityPlacesBinding
import com.algolia.instantsearch.helper.android.list.autoScrollToStart
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.SearcherPlaces
import com.algolia.search.model.places.Country
import com.algolia.search.model.places.PlaceType
import com.algolia.search.model.places.PlacesQuery
import com.algolia.search.model.search.Language

class PlacesActivity : AppCompatActivity() {

    val query = PlacesQuery(
        type = PlaceType.City,
        hitsPerPage = 10,
        aroundLatLngViaIP = false,
        countries = listOf(Country.France)
    )
    val searcher = SearcherPlaces(query = query, language = Language.English)
    val searchBox = SearchBoxConnector(searcher)
    val adapter = Adapter()
    val connection = ConnectionHandler(searchBox)

    private lateinit var binding: ActivityPlacesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlacesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        connection += searchBox.connectView(SearchBoxViewAppCompat(binding.searchView))
        connection += searcher.connectHitsView(adapter) { hits -> hits.hits }

        binding.placesList.let {
            it.itemAnimator = null
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(this)
            it.autoScrollToStart(adapter)
        }

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        connection.clear()
    }
}
