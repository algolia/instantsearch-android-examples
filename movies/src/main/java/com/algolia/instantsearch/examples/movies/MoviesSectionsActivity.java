package com.algolia.instantsearch.examples.movies;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.algolia.instantsearch.helpers.InstantSearch;
import com.algolia.instantsearch.ui.views.SearchBox;

public class MoviesSectionsActivity extends MoviesActivity {

    private InstantSearch instantSearchMovies;
    private InstantSearch instantSearchActors;

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        final String query = intent.getStringExtra(EXTRA_QUERY);
        Toast.makeText(this, "Applying query " + query, Toast.LENGTH_SHORT).show();
        this.<SearchBox>findViewById(R.id.searchBox).setQuery(query, query != null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_sections);
        initWithLayout();

        instantSearchMovies = new InstantSearch(this, searcherMovies);
        instantSearchMovies.registerSearchView(this, searchBoxViewModel);
        instantSearchActors = new InstantSearch(this, searcherActors);
        instantSearchActors.registerSearchView(this, searchBoxViewModel);

        // If the activity was started with an intent, apply any query it contains
        setQueryFromIntent(getIntent());
    }
}
