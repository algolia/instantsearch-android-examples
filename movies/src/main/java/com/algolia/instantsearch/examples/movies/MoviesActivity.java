package com.algolia.instantsearch.examples.movies;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algolia.instantsearch.helpers.InstantSearch;
import com.algolia.instantsearch.helpers.Searcher;
import com.algolia.instantsearch.ui.views.SearchBox;

public class MoviesActivity extends AppCompatActivity {

    private static final String ALGOLIA_APP_ID = "latency";
    private static final String ALGOLIA_INDEX_ACTORS = "actors";
    private static final String ALGOLIA_INDEX_MOVIES = "movies";
    private static final String ALGOLIA_API_KEY = "d0a23086ed4be550f70be98c0acf7d74";

    private Searcher searcherMovies;
    private Searcher searcherActors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize a Searcher with your credentials and an index name
        searcherMovies = Searcher.create(ALGOLIA_APP_ID, ALGOLIA_API_KEY, ALGOLIA_INDEX_MOVIES);
        searcherActors = Searcher.create(ALGOLIA_APP_ID, ALGOLIA_API_KEY, ALGOLIA_INDEX_ACTORS);

        // Set up the ViewPager with an adapter that will return a fragment
        // for each of the two primary sections of the activity.
        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = findViewById(R.id.tabs);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }


    @Override
    protected void onDestroy() {
        searcherMovies.destroy();
        searcherActors.destroy();
        super.onDestroy();
    }

    private void linkFragmentToSearcher(LayoutFragment layoutFragment) {
        Searcher searcher = (layoutFragment instanceof MoviesFragment) ? searcherMovies : searcherActors;

        // link the Searcher to the Fragment's UI
        final SearchBox searchBox = findViewById(R.id.searchBox);
        new InstantSearch(this, searcher, layoutFragment).registerSearchView(this, searchBox);
        // Show results for empty query (on app launch) / voice query (from intent)
        searcher.search(getIntent());
    }

    public static abstract class LayoutFragment extends Fragment {
        private final int layout;

        public LayoutFragment(@LayoutRes int layout) {
            this.layout = layout;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(layout, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            final MoviesActivity activity = (MoviesActivity) getActivity();
            assert activity != null;
            activity.linkFragmentToSearcher(this);
        }
    }


    public static class MoviesFragment extends LayoutFragment {
        public MoviesFragment() {
            super(R.layout.fragment_movies);
        }
    }

    public static class ActorsFragment extends LayoutFragment {
        public ActorsFragment() {
            super(R.layout.fragment_actors);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            switch (position) {
                case 0:
                    return new MoviesFragment();
                default:
                    return new ActorsFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}