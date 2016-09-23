package com.algolia.instantsearch.examples.ecommerce;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RatingBar;

import com.algolia.instantsearch.views.AlgoliaHitView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A RatingBar that will transform ratings before displaying them.
 * <p>
 * For most products, bestSellingRank goes from 4 to 32K,
 * this widget will display it as a 0 to 5 rating.
 */
public class PreprocessedRatingBar extends RatingBar implements AlgoliaHitView {

    public static final float MIN_BEST_SELLING_RANK = 4;
    public static final float MAX_BEST_SELLING_RANK = 32691;

    public PreprocessedRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void onUpdateView(JSONObject result) {
        try {
            float rank = getRankForResult(result);
            setRating(rank);
        } catch (JSONException e) {
            Log.e("DividingRatingBar", "Error while getting attribute bestSellingRank");
            e.printStackTrace();
        }
    }

    private float getRankForResult(JSONObject result) throws JSONException {
        final int bestSellingRank = result.getInt("bestSellingRank");
        float rank;
        if (bestSellingRank > MAX_BEST_SELLING_RANK) {
            rank = 0;
        } else {
            float percentRank = (MIN_BEST_SELLING_RANK + MAX_BEST_SELLING_RANK - bestSellingRank) / MAX_BEST_SELLING_RANK;
            rank = percentRank * 5;
        }
        return rank;
    }
}
