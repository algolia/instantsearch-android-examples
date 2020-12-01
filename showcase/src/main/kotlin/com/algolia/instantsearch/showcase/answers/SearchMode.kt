package com.algolia.instantsearch.showcase.answers

import android.content.Context
import androidx.annotation.StringRes
import com.algolia.instantsearch.showcase.R

enum class SearchMode(@StringRes private val value: Int) {
    HYBRID(R.string.hybrid),
    STANDARD(R.string.standard),
    QA(R.string.q_a);

    fun getValue(context: Context): String {
        return context.getString(value)
    }

    companion object {

        fun asStringList(context: Context): List<String> {
            return values().map { searchMode -> searchMode.getValue(context) }
        }
    }
}
