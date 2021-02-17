package com.algolia.instantsearch.showcase.dynamic.components

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.IdRes
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.showcase.dynamic.components.DynamicSortBanner.State.All
import com.algolia.instantsearch.showcase.dynamic.components.DynamicSortBanner.State.Relevant

class DynamicSortBanner(
    private val banner: View,
    private val hintButton: Button,
    private val hintLabel: TextView,
    private var state: State? = null
) : DynamicSortView {

    init {
        hintButton.setOnClickListener { didToggle?.invoke() }
        updateView(state)
    }

    override var didToggle: (() -> Unit)? = null

    override fun currentPriority(priority: DynamicSortPriority?) {
        state = when (priority) {
            DynamicSortPriority.Relevancy -> Relevant
            DynamicSortPriority.HitsCount -> All
            else -> null
        }
        updateView(state)
    }

    private fun updateView(state: State?) {
        if (state == null) {
            banner.visibility = View.GONE
        } else {
            banner.visibility = View.VISIBLE
            hintLabel.setText(state.hintLabelText)
            hintButton.setText(state.hintButtonText)
        }
    }

    enum class State(
        @IdRes val hintButtonText: Int,
        @IdRes val hintLabelText: Int,
    ) {
        Relevant(R.string.show_relevant_results_hint, R.string.show_all_results),
        All(R.string.show_all_results_hint, R.string.show_relevant_results)
    }
}
