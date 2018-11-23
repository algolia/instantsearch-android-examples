package com.algolia.instantsearch.examples.movies;

import android.os.Bundle;

import com.algolia.instantsearch.ui.helpers.InstantSearch;

public class MoviesSectionsActivity extends MoviesActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_sections);
        initWithLayout();

        new InstantSearch(this, searcherMovies).registerSearchView(this, searchBoxViewModel);
        new InstantSearch(this, searcherActors).registerSearchView(this, searchBoxViewModel);

        // If the activity was started with an intent, apply any query it contains
        setQueryFromIntent(getIntent());
    }
}