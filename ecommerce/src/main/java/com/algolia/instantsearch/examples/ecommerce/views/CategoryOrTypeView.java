package com.algolia.instantsearch.examples.ecommerce.views;

import android.content.Context;
import android.util.AttributeSet;

import com.algolia.instantsearch.views.AlgoliaHitView;

import org.json.JSONException;
import org.json.JSONObject;


public class CategoryOrTypeView extends NotNullView implements AlgoliaHitView {
    public CategoryOrTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onUpdateView(JSONObject result) {
        try {
            String category = result.getString("category");
            if (!isNull(category)) {
                setText(category);
            } else {
                final String type = result.getString("type");
                setText(type);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
