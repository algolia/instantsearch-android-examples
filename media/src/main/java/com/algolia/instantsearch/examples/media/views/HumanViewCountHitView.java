package com.algolia.instantsearch.examples.media.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.algolia.instantsearch.ui.views.AlgoliaHitView;

import org.json.JSONObject;

import java.text.DecimalFormat;


public class HumanViewCountHitView extends TextView implements AlgoliaHitView {
    public HumanViewCountHitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onUpdateView(JSONObject result) {
        String viewsAttr = result.optString("views", "0");
        long views = Long.parseLong(viewsAttr);
        setText(" • " + metricPrefix(views) + " views");
    }

    private String metricPrefix(long value) {
        if (value > 1000000000) {
            return (double) Double.valueOf(new DecimalFormat("#.#").format(value / 1000000000f)) + " B";
        } else if (value > 1000000) {
            return value / 1000000 + " M";
        } else return String.valueOf(value);
    }
}
