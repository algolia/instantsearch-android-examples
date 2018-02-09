package com.algolia.instantsearch.examples.movies;

import android.os.Bundle;

import com.algolia.instantsearch.helpers.InstantSearch;

public class MoviesSectionsActivity extends MoviesActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_sections);
        initWithLayout();

        InstantSearch instantSearchMovies = new InstantSearch(this, searcherMovies);
        instantSearchMovies.registerSearchView(this, searchBoxViewModel);
        InstantSearch instantSearchActors = new InstantSearch(this, searcherActors);
        instantSearchActors.registerSearchView(this, searchBoxViewModel);

        // If the activity was started with an intent, apply any query it contains
        setQueryFromIntent(getIntent());
    }
}