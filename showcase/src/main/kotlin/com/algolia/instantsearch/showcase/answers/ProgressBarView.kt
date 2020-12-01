package com.algolia.instantsearch.showcase.answers

import android.view.View
import android.widget.ProgressBar
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.loading.LoadingView

class ProgressBarView(
    private val progressBar: ProgressBar
) : LoadingView {

    override var onReload: Callback<Unit>? = null

    override fun setIsLoading(isLoading: Boolean) {
        when (isLoading) {
            true -> progressBar.visibility = View.VISIBLE
            false -> progressBar.visibility = View.INVISIBLE
        }
    }
}
