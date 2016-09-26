package com.algolia.instantsearch.examples.ecommerce;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;


public class WrappedTextView extends TextView {
    private String prefix;
    private String suffix;
    private CharSequence text;

    public WrappedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.WrappedTextView);
        setAttributes(styledAttributes);
    }

    public WrappedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.WrappedTextView, defStyle, 0);
        setAttributes(styledAttributes);
    }

    private void setAttributes(TypedArray styledAttributes) {
        prefix = styledAttributes.getString(R.styleable.WrappedTextView_prefix);
        if (prefix == null) {
            prefix = "";
        }
        suffix = styledAttributes.getString(R.styleable.WrappedTextView_suffix);
        if (suffix == null) {
            suffix = "";
        }
        styledAttributes.recycle();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text != null && !text.equals("") ? prefix + text + suffix : text, type);
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
