package com.algolia.instantsearch.examples.ecommerce.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.Toast;

import com.algolia.instantsearch.examples.ecommerce.R;
import com.algolia.instantsearch.views.AlgoliaHitView;

import org.json.JSONException;
import org.json.JSONObject;

public class PriceView extends TextView implements AlgoliaHitView {
    private final Context context;

    public static final String SYMBOL_MONEY = "$";

    public PriceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @SuppressLint("SetTextI18n") /* This will only display US prices */
    @Override
    public void onUpdateView(JSONObject result) {
        try {
            double salePrice = result.getDouble("salePrice");
            double promoPrice = result.getDouble("promoPrice");
            double lowestPrice = Math.min(salePrice, promoPrice);

            final String priceText = SYMBOL_MONEY + lowestPrice;
            final SpannableStringBuilder finalText = new SpannableStringBuilder(priceText);

            finalText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrice)), 0, priceText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            finalText.setSpan(new StyleSpan(Typeface.BOLD), 0, priceText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            if (salePrice != promoPrice) {
                finalText.append(" ").append(SYMBOL_MONEY).append(Double.toString(salePrice));
                finalText.setSpan(new StrikethroughSpan(), priceText.length() + 1, finalText.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            }
            setText(finalText, BufferType.SPANNABLE);
        } catch (JSONException e) {
            Toast.makeText(context, "Error parsing result:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
