package com.algolia.instantsearch.examples.querysuggestions

import android.content.Context
import android.util.AttributeSet
import com.algolia.instantsearch.helpers.Highlighter
import com.algolia.instantsearch.ui.views.AlgoliaHitView
import org.json.JSONException
import org.json.JSONObject


class CategoryOrTypeView(context: Context, attrs: AttributeSet) : NotNullTextView(context, attrs), AlgoliaHitView {

    override fun onUpdateView(result: JSONObject) {
        try {
            val category = result.optString("category")
            val attributeToHighlight = if (!isNull(category)) "category" else "type"

            text = Highlighter.getDefault().setInput(result, attributeToHighlight).setStyle(context).render()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}
