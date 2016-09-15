package com.algolia.instantsearch.examples.media.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.algolia.instantsearch.views.AlgoliaHitView;

import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;


public class TimestampHitView extends TextView implements AlgoliaHitView {
    public TimestampHitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onUpdateView(JSONObject result) {
        String timestamp = result.optString("published");
        setText(new PrettyTime().format(new Date(Long.parseLong(timestamp) * 1000)));
    }
}
