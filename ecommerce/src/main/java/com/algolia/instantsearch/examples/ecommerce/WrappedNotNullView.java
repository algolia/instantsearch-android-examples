package com.algolia.instantsearch.examples.ecommerce;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;


public class WrappedNotNullView extends NotNullView {
    private String prefix;
    private String suffix;
    private CharSequence text;

    public WrappedNotNullView(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.WrappedNotNullView);
        setAttributes(styledAttributes);
    }

    public WrappedNotNullView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.WrappedNotNullView, defStyle, 0);
        setAttributes(styledAttributes);
    }

    private void setAttributes(TypedArray styledAttributes) {
        prefix = styledAttributes.getString(R.styleable.WrappedNotNullView_prefix);
        if (prefix == null) {
            prefix = "";
        }
        suffix = styledAttributes.getString(R.styleable.WrappedNotNullView_suffix);
        if (suffix == null) {
            suffix = "";
        }
        styledAttributes.recycle();
    }

    @Override
    public void setText(CharSequence text, TextView.BufferType type) {
        if (isNull(text)) {
            super.setText(text, type);
        } else {
            super.setText(prefix + text + suffix, type);
        }
        this.text = text;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        setText(text);
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
        setText(text);
    }

}
