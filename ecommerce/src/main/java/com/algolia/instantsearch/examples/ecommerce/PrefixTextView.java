package com.algolia.instantsearch.examples.ecommerce;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;


public class PrefixTextView extends TextView {
    private String prefix;

    public PrefixTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.PrefixTextView);
        setPrefix(styledAttributes);
    }

    public PrefixTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.PrefixTextView, defStyle, 0);
        setPrefix(styledAttributes);
    }

    private void setPrefix(TypedArray styledAttributes) {
        prefix = styledAttributes.getString(R.styleable.PrefixTextView_prefix);
        if (prefix == null) {
            prefix = "";
        }
        styledAttributes.recycle();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text != null && !text.equals("") ? prefix + text : text, type);
    }
}
