package com.algolia.instantsearch.examples.media;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.algolia.instantsearch.InstantSearchHelper;
import com.algolia.instantsearch.Searcher;
import com.algolia.instantsearch.events.ErrorEvent;
import com.algolia.instantsearch.ui.FilterResultsFragment;
import com.algolia.search.saas.Client;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {

    private static final String ALGOLIA_APP_ID = "latency";
    private static final String ALGOLIA_INDEX_NAME = "youtube";
    private static final String ALGOLIA_API_KEY = "0c9899197c49f80b183adc0f68ea8d78";

    private Searcher searcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searcher = new Searcher(new Client(ALGOLIA_APP_ID, ALGOLIA_API_KEY).initIndex(ALGOLIA_INDEX_NAME));
        new InstantSearchHelper(this, menu, R.id.action_search, searcher);
        searcher.search(); //Show results for empty query on startup
        menu.findItem(R.id.action_search).expandActionView(); //open SearchBar on startup
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            final FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.findFragmentByTag(FilterResultsFragment.TAG) == null) {
                new FilterResultsFragment().with(this, searcher)
                        .addSeekBar("views", 0, 1000000000, 100)
                        .addSeekBar("rating", "stars", 0, 5, 100)
                        .addCheckBox("cc", "Closed Captions (CC)", true)
                        .addCheckBox("4k", "4K", true)
                        .addCheckBox("hd", "HD", true)
                        .show(fragmentManager, FilterResultsFragment.TAG);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent event) {
        Log.e("PLN", "Error searching" + event.query.getQuery() + ":" + event.error.getLocalizedMessage());
    }
}