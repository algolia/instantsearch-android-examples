package com.algolia.instantsearch.examples.ecommerce;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.algolia.instantsearch.helpers.InstantSearch;
import com.algolia.instantsearch.helpers.Searcher;
import com.algolia.instantsearch.ui.views.SearchBox;
import com.algolia.search.saas.MirroredIndex;
import com.algolia.search.saas.OfflineClient;
import com.algolia.search.saas.Query;
import com.algolia.search.saas.SyncListener;
import com.squareup.leakcanary.RefWatcher;

public class EcommerceActivity extends AppCompatActivity {

    private static final String ALGOLIA_APP_ID = "latency";
    private static final String ALGOLIA_INDEX_NAME = "bestbuy_promo";
    private static final String ALGOLIA_API_KEY = "91e5b0d48d0ea9c1eb7e7e063d5c7750";

    private FilterResultsWindow filterResultsWindow;
    private Drawable arrowDown;
    private Drawable arrowUp;
    private Button buttonFilter;
    private Searcher searcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecommerce);

        final String TAG = "OFFLINE";
        OfflineClient client = new OfflineClient(this, ALGOLIA_APP_ID, "7f3c958936c885de959cb4c55e45751e");
        client.enableOfflineMode("AisEAQH/qZqmBf/s9qQFABpwYXVsbG91aXMubmVjaEBhbGdvbGlhLmNvbQEqMC4CFQCN+t2zKoat7gw5KD81N/kwXrnO/gIVAMRcrN9UBivanjEXUohjJWNRDGbl");
        final MirroredIndex test = client.getIndex(ALGOLIA_INDEX_NAME);
        test.setMirrored(true);
        test.setOfflineFallbackTimeout(1);
        test.addDataSelectionQuery(new MirroredIndex.DataSelectionQuery(new Query("iPhone"), 2));
        test.addSyncListener(new SyncListener() {
            @Override
            public void syncDidStart(MirroredIndex index) {
                Log.e(TAG, "Syncing");
            }

            @Override
            public void syncDidFinish(MirroredIndex index, Throwable error, MirroredIndex.SyncStats stats) {
                searcher = Searcher.create(test);
                new InstantSearch(EcommerceActivity.this, searcher); // Initialize InstantSearch in this activity with searcher
                searcher.search(getIntent()); // Show results for empty query (on app launch) / voice query (from intent)

                ((SearchBox) findViewById(R.id.searchBox)).disableFullScreen(); // disable fullscreen input UI on landscape
                filterResultsWindow = new FilterResultsWindow.Builder(EcommerceActivity.this, searcher)
                        .addSeekBar("salePrice", "initial price", 100)
                        .addSeekBar("customerReviewCount", "reviews", 100)
                        .addCheckBox("promoted", "Has a discount", true)
                        .addSeekBar("promoPrice", "price with discount", 100)
                        .build();

                buttonFilter = findViewById(R.id.btn_filter);
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
        });
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                test.sync();

            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        searcher.search(intent);
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
        searcher.destroy();
        toggleArrow(buttonFilter, false);
        super.onDestroy();
        RefWatcher refWatcher = EcommerceApplication.getRefWatcher(this);
        refWatcher.watch(this);
        refWatcher.watch(findViewById(R.id.hits));
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
