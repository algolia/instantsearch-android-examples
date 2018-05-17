package com.algolia.instantsearch.examples.querysuggestions

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.AppCompatTextView
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.AttributeSet
import android.widget.TextView
import android.widget.Toast
import com.algolia.instantsearch.ui.views.AlgoliaHitView
import org.json.JSONException
import org.json.JSONObject

class PriceView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs), AlgoliaHitView {

    @SuppressLint("SetTextI18n")
    /* This will only display US prices */
    override fun onUpdateView(result: JSONObject) {
        try {
            val price = result.getDouble("price")

            val priceText = SYMBOL_MONEY + price
            val finalText = SpannableStringBuilder(priceText)

            @Suppress("DEPRECATION" /* Alternative getColor(c, theme) requires API >= 23 */)
            finalText.setSpan(ForegroundColorSpan(resources.getColor(R.color.colorPrice)), 0, priceText.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            finalText.setSpan(StyleSpan(Typeface.BOLD), 0, priceText.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            setText(finalText, TextView.BufferType.SPANNABLE)
        } catch (e: JSONException) {
            Toast.makeText(context, "Error parsing result:" + e.message, Toast.LENGTH_SHORT).show()
        }

    }

    companion object {
        const val SYMBOL_MONEY = "$"
    }
}
