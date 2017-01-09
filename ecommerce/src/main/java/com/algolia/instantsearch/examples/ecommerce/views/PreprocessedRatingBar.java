package com.algolia.instantsearch.examples.ecommerce.views;

import android.content.Context;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.AttributeSet;
import android.util.Log;

import com.algolia.instantsearch.ui.views.AlgoliaHitView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A RatingBar that will transform ratings before displaying them.
 * <p>
 * For most products, bestSellingRank goes from 4 to 32K,
 * this widget will display it as a 0 to 5 rating.
 */
public class PreprocessedRatingBar extends AppCompatRatingBar implements AlgoliaHitView {

    public static final float MIN_BEST_SELLING_RANK = 4;
    public static final float MAX_BEST_SELLING_RANK = 32691;

    public PreprocessedRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void onUpdateView(JSONObject result) {
        try {
            final int customerReviewCount = result.optInt("customerReviewCount", 0);

            if (customerReviewCount == 0) {
                setVisibility(GONE);
            } else {
                setVisibility(VISIBLE);
                float rank = getRankForResult(result);
                Log.d("PreprocessedRatingBar", "customerReviewCount " + customerReviewCount + " -> rating=" + rank);
                setRating(rank);
            }
        } catch (JSONException e) {
            Log.e("PreprocessedRatingBar", "Error while getting attribute bestSellingRank");
            e.printStackTrace();
        }
    }

    private float getRankForResult(JSONObject result) throws JSONException {
        final int bestSellingRank = result.getInt("bestSellingRank");
        if (bestSellingRank < 50) {
            return 5;
        } else if (bestSellingRank < 500) {
            return 4.5f;
        } else if (bestSellingRank < 2500) {
            return 4.0f;
        } else if (bestSellingRank < 5000) {
            return 3.5f;
        } else if (bestSellingRank < 10000) {
            return 3.0f;
        } else if (bestSellingRank < 15000) {
            return 2.5f;
        } else if (bestSellingRank < 20000) {
            return 2.0f;
        } else if (bestSellingRank < 25000) {
            return 1.5f;
        } else if (bestSellingRank < 30000) {
            return 1.0f;
        } else if (bestSellingRank < MAX_BEST_SELLING_RANK) {
            return 0.5f;
        } else {
            return 0;
        }
    }
}
