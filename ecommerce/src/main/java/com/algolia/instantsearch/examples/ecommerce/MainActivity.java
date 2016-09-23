package com.algolia.instantsearch.examples.ecommerce;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.algolia.instantsearch.InstantSearchHelper;
import com.algolia.instantsearch.Searcher;
import com.algolia.search.saas.Client;

public class MainActivity extends AppCompatActivity {

    private static final String ALGOLIA_APP_ID = "latency";
    private static final String ALGOLIA_INDEX_NAME = "ikea";
    private static final String ALGOLIA_API_KEY = "6be0576ff61c053d5f9a3225e2a90f76";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Searcher searcher = new Searcher(new Client(ALGOLIA_APP_ID, ALGOLIA_API_KEY).initIndex(ALGOLIA_INDEX_NAME));
        InstantSearchHelper helper = new InstantSearchHelper(this, searcher);
    }
}
