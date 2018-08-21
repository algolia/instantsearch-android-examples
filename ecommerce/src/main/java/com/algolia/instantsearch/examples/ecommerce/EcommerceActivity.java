package com.algolia.instantsearch.examples.ecommerce;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.algolia.instantsearch.helpers.InstantSearch;
import com.algolia.instantsearch.helpers.Searcher;
import com.algolia.instantsearch.ui.views.SearchBox;
import com.algolia.instantsearch.voice.Voice;
import com.algolia.instantsearch.voice.VoiceInput;
import com.algolia.instantsearch.voice.ui.PermissionDialogFragment;
import com.algolia.instantsearch.voice.ui.VoiceDialogFragment;
import com.squareup.leakcanary.RefWatcher;

import java.util.List;

import static android.Manifest.permission.RECORD_AUDIO;
import static com.algolia.instantsearch.voice.Voice.isPermissionGranted;
import static com.algolia.instantsearch.voice.Voice.isRecordPermissionWithResults;
import static com.algolia.instantsearch.voice.Voice.shouldExplainPermission;
import static com.algolia.instantsearch.voice.ui.PermissionDialogFragment.ID_REQ_VOICE_PERM;

public class EcommerceActivity extends AppCompatActivity implements VoiceInput.VoiceResultsListener {
    private static final String ALGOLIA_APP_ID = "latency";
    private static final String ALGOLIA_INDEX_NAME = "bestbuy_promo";
    private static final String ALGOLIA_API_KEY = "91e5b0d48d0ea9c1eb7e7e063d5c7750";

    private FilterResultsWindow filterResultsWindow;
    private Drawable arrowDown;
    private Drawable arrowUp;
    private Button buttonFilter;
    private Searcher searcher;

    // region Lifecycle
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecommerce);

        searcher = Searcher.create(ALGOLIA_APP_ID, ALGOLIA_API_KEY, ALGOLIA_INDEX_NAME);
        new InstantSearch(this, searcher); // Initialize InstantSearch in this activity with searcher
        searcher.search(getIntent()); // Show results for empty query (on app launch) / voice query (from intent)

        ((SearchBox) findViewById(R.id.searchBox)).disableFullScreen(); // disable fullscreen input UI on landscape
        filterResultsWindow = new FilterResultsWindow.Builder(this, searcher)
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

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
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

    @Override
    public void onVoiceResults(@NonNull List<String> matches) {
        searcher.search(matches.get(0));
    }
    // endregion

    // region Permission handling
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (isRecordPermissionWithResults(requestCode, grantResults)) {
            if (isPermissionGranted(grantResults)) showVoiceOverlay();
            else if (shouldExplainPermission(this)) showPermissionRationale();
            else showPermissionManualInstructions();
        }
    }

    private void showPermissionRationale() {
        Snackbar.make(findViewById(R.id.mic), R.string.voice_search_rationale, Snackbar.LENGTH_LONG)
                .setAction(R.string.voice_search_button_again, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(EcommerceActivity.this, new String[]{RECORD_AUDIO}, ID_REQ_VOICE_PERM);
                    }
                }).show();
    }

    private void showPermissionManualInstructions() {
        final View micView = findViewById(R.id.mic);
        Snackbar snackbar = Snackbar.make(micView, R.string.voice_search_disabled_rationale, Snackbar.LENGTH_LONG)
                .setAction(R.string.voice_search_button_enable, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(micView, R.string.voice_search_disabled_instructions, Snackbar.LENGTH_SHORT)
                                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                                    @Override
                                    public void onDismissed(Snackbar transientBottomBar, int event) {
                                        startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                                .setData(Uri.fromParts("package", getPackageName(), null)));
                                    }
                                }).show();
                    }
                });
        ((TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setMaxLines(2);
        snackbar.show();
    }
    // endregion

    // region UI
    public void onTapMic(@NonNull View view) {
        if (!Voice.hasRecordPermission(this)) {
            showPermissionOverlay();
        } else {
            showVoiceOverlay();
        }
    }

    private void toggleArrow(Button b, boolean isUp) {
        final Drawable[] currentDrawables = b.getCompoundDrawables();
        b.setCompoundDrawablesWithIntrinsicBounds(currentDrawables[0], currentDrawables[1], getArrowDrawable(isUp), currentDrawables[3]);
    }

    private Drawable getArrowDrawable(boolean isUp) {
        if (isUp) arrowUp = ContextCompat.getDrawable(this, R.drawable.arrow_up_flat);
        else arrowDown = ContextCompat.getDrawable(this, R.drawable.arrow_down_flat);
        return isUp ? arrowUp : arrowDown;
    }

    private void showVoiceOverlay() {
        VoiceDialogFragment frag = new VoiceDialogFragment();
        frag.setSuggestions("iPhone case", "Running shoes"); //TODO: use query suggestions?
        frag.show(getSupportFragmentManager(), "voice");
    }

    private void showPermissionOverlay() {
        PermissionDialogFragment frag = new PermissionDialogFragment();
        frag.setTitle("You can use voice search to find products");
        frag.show(getSupportFragmentManager(), "permission");
    }
    // endregion
}