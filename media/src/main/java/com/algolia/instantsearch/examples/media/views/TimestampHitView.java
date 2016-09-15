package com.algolia.instantsearch.examples.media.views;

import android.content.Context;
import android.util.AttributeSet;

import com.algolia.instantsearch.views.AlgoliaHitView;
import com.github.curioustechizen.ago.RelativeTimeTextView;

import org.json.JSONObject;


public class TimestampHitView extends RelativeTimeTextView implements AlgoliaHitView {
    public TimestampHitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onUpdateView(JSONObject result) {
        String timestamp = result.optString("published");
        setReferenceTime(Long.parseLong(timestamp) * 1000 /* milliseconds timestamp required */);
    }
}
