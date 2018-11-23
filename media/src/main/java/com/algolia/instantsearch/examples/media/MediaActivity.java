package com.algolia.instantsearch.examples.media;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.algolia.instantsearch.events.ErrorEvent;
import com.algolia.instantsearch.ui.helpers.InstantSearch;
import com.algolia.instantsearch.core.helpers.Searcher;
import com.algolia.instantsearch.ui.views.SearchBox;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MediaActivity extends AppCompatActivity {

    private static final String ALGOLIA_APP_ID = "latency";
    public static final String ALGOLIA_INDEX_NAME = "youtube";
    private static final String ALGOLIA_API_KEY = "0c9899197c49f80b183adc0f68ea8d78";

    private Searcher searcher;

    private FilterResultsFragment filterResultsFragment;
    private SearchView searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        EventBus.getDefault().register(this);

        // Initialize a Searcher with your credentials and an index name
        searcher = Searcher.create(ALGOLIA_APP_ID, ALGOLIA_API_KEY, ALGOLIA_INDEX_NAME);

        // Create the FilterResultsFragment here so it can set the appropriate facets on the Searcher
        filterResultsFragment = FilterResultsFragment.get(searcher)
                .addSeekBar("views", 100)
                .addSeekBar("rating", "stars", 100)
                .addCheckBox("cc", "Closed Captions (CC)", true)
                .addCheckBox("4k", "4K", true)
                .addCheckBox("hd", "HD", true);
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        searcher.search(intent); // Show results for voice query (from intent)
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_media, menu);

        new InstantSearch(this, menu, R.id.action_search, searcher); // link the Searcher to the UI
        searcher.search(getIntent()); // Show results for empty query (on app launch) / voice query (from intent)

        final MenuItem itemSearch = menu.findItem(R.id.action_search);
        searchBox = (SearchBox) itemSearch.getActionView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            itemSearch.expandActionView(); //open SearchBar on startup
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            final FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.findFragmentByTag(FilterResultsFragment.TAG) == null) {
                filterResultsFragment.show(fragmentManager, FilterResultsFragment.TAG);
                hideKeyboard();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent event) {
        Toast.makeText(this, "Error searching" + event.query.getQuery() + ":" + event.error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    private void hideKeyboard() {
        searchBox.clearFocus();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}