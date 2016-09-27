package com.algolia.instantsearch.examples.ecommerce;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.algolia.instantsearch.InstantSearchHelper;
import com.algolia.instantsearch.Searcher;
import com.algolia.instantsearch.ui.FilterResultsWindow;
import com.algolia.instantsearch.views.SearchBox;
import com.algolia.search.saas.Client;

public class MainActivity extends AppCompatActivity {

    private static final String ALGOLIA_APP_ID = "latency";
    private static final String ALGOLIA_INDEX_NAME = "bestbuy_promo";
    private static final String ALGOLIA_API_KEY = "91e5b0d48d0ea9c1eb7e7e063d5c7750";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Searcher searcher = new Searcher(new Client(ALGOLIA_APP_ID, ALGOLIA_API_KEY).initIndex(ALGOLIA_INDEX_NAME))
                .search(); // Show results for empty query on app launch
        new InstantSearchHelper(this, searcher); // Initialize InstantSearch in this activity with searcher

        ((SearchBox) findViewById(R.id.searchBox)).disableFullScreen(); // disable fullscreen input UI on landscape

        new FilterResultsWindow(this, searcher)
                .addSeekBar("salePrice", "price", 0d, 10000d, 100) //TODO: Accept int/float as well
                .setToggleButton((Button) findViewById(R.id.btn_filter));
    }
}
