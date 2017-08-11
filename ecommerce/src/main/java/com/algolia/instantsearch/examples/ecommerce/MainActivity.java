package com.algolia.instantsearch.examples.ecommerce;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.algolia.instantsearch.helpers.Searcher;
import com.algolia.instantsearch.helpers.InstantSearch;
import com.algolia.instantsearch.ui.views.SearchBox;

public class MainActivity extends AppCompatActivity {

    private static final String ALGOLIA_APP_ID = "latency";
    private static final String ALGOLIA_INDEX_NAME = "bestbuy_promo";
    private static final String ALGOLIA_API_KEY = "91e5b0d48d0ea9c1eb7e7e063d5c7750";

    private FilterResultsWindow filterResultsWindow;
    private Drawable arrowDown;
    private Drawable arrowUp;
    private Button buttonFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Searcher searcher = Searcher.create(ALGOLIA_APP_ID, ALGOLIA_API_KEY, ALGOLIA_INDEX_NAME);
        new InstantSearch(this, searcher); // Initialize InstantSearch in this activity with searcher

        searcher.search(); // Show results for empty query on app launch

        ((SearchBox) findViewById(R.id.searchBox)).disableFullScreen(); // disable fullscreen input UI on landscape

        filterResultsWindow = new FilterResultsWindow.Builder(this, searcher)
                .addSeekBar("salePrice", "initial price", 100)
                .addSeekBar("customerReviewCount", "reviews", 100)
                .addCheckBox("promoted", "Has a discount", true)
                .addSeekBar("promoPrice", "price with discount", 100)
                .build();

        buttonFilter = (Button) findViewById(R.id.btn_filter);
        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean willDisplay = !filterResultsWindow.isShowing();
                if (willDisplay) {
                    filterResultsWindow.showAsDropDown(buttonFilter);
                } else {
                    filterResultsWindow.dismiss();
                }
                toggleArrow(buttonFilter, willDisplay);
            }
        });
    }

    @Override
    protected void onStop() {
        filterResultsWindow.dismiss();
        toggleArrow(buttonFilter, false);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        filterResultsWindow.dismiss();
        toggleArrow(buttonFilter, false);
        super.onDestroy();
    }

    private void toggleArrow(Button b, boolean up) {
        final Drawable[] currentDrawables = b.getCompoundDrawables();
        final Drawable newDrawable;
        if (up) {
            if (arrowUp == null) {
                arrowUp = getResources().getDrawable(R.drawable.arrow_up_flat);
            }
            newDrawable = arrowUp;
        } else {
            if (arrowDown == null) {
                arrowDown = getResources().getDrawable(R.drawable.arrow_down_flat);
            }
            newDrawable = arrowDown;
        }
        b.setCompoundDrawablesWithIntrinsicBounds(currentDrawables[0], currentDrawables[1], newDrawable, currentDrawables[3]);

    }
}
