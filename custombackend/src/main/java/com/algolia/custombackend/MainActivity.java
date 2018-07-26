package com.algolia.custombackend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.algolia.custombackend.custombackend.CustomClient;
import com.algolia.instantsearch.helpers.InstantSearch;
import com.algolia.instantsearch.helpers.Searcher;
import com.algolia.instantsearch.ui.views.SearchBox;

public class MainActivity extends AppCompatActivity {

    private Searcher searcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CustomClient customClient = new CustomClient();

        searcher = Searcher.create(customClient);
        new InstantSearch(this, searcher); // Initialize InstantSearch in this activity with searcher
        searcher.search(getIntent()); // Show results for empty query (on app launch) / voice query (from intent)

        ((SearchBox) findViewById(R.id.searchBox)).disableFullScreen(); // disable fullscreen input UI on landsca
    }
}
