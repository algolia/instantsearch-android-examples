package com.algolia.instantsearch.examples.querysuggestions

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView

open class NotNullTextView : android.support.v7.widget.AppCompatTextView {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun setText(text: CharSequence?, type: TextView.BufferType) {
        if (isNull(text)) {
            visibility = View.GONE
            super.setText("", type)
        } else {
            visibility = View.VISIBLE
            super.setText(text!!, type)
        }
    }

    companion object {
        internal fun isNull(text: CharSequence?): Boolean = text == null || text == "" || text.toString() == "null"
    }
}
