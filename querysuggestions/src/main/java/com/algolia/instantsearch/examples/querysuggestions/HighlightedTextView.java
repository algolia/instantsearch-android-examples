package com.algolia.instantsearch.examples.querysuggestions;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

public class HighlightedTextView extends android.support.v7.widget.AppCompatTextView {
    public HighlightedTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(Html.fromHtml(String.valueOf(text)), type);
    }
}
