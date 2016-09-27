package com.algolia.instantsearch.examples.ecommerce.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class NotNullView extends TextView {
    public NotNullView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotNullView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static boolean isNull(CharSequence text) {
        return text == null || text.equals("") || text.equals("null");
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (isNull(text)) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
            super.setText(text, type);
        }
    }
}
