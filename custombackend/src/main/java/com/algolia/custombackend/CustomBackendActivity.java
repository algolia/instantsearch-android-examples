package com.algolia.custombackend;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.algolia.custombackend.custombackend.CustomClient;
import com.algolia.instantsearch.core.helpers.Searcher;
import com.algolia.instantsearch.ui.helpers.InstantSearch;
import com.algolia.instantsearch.ui.views.SearchBox;

public class CustomBackendActivity extends AppCompatActivity {

    private Searcher searcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_backend);
        CustomClient customClient = new CustomClient();

        searcher = Searcher.create(customClient);
        new InstantSearch(this, searcher); // Initialize InstantSearch in this activity with searcher
        searcher.search(getIntent()); // Show results for empty query (on app launch) / voice query (from intent)

        ((SearchBox) findViewById(R.id.searchBox)).disableFullScreen(); // disable fullscreen input UI on landscape
    }
}
