package com.algolia.instantsearch.examples.media.views;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.algolia.instantsearch.ui.views.AlgoliaHitView;

import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;


public class TimestampHitView extends AppCompatTextView implements AlgoliaHitView {
    public TimestampHitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onUpdateView(JSONObject result) {
        String timestamp = result.optString("published");
        setText(new PrettyTime().format(new Date(Long.parseLong(timestamp) * 1000)));
    }
}
