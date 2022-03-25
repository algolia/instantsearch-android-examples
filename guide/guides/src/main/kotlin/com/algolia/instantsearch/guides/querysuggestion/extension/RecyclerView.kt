package com.algolia.instantsearch.guides.querysuggestion.extension

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.android.list.autoScrollToStart

internal fun RecyclerView.configure(recyclerViewAdapter: RecyclerView.Adapter<*>) {
    visibility = View.VISIBLE
    layoutManager = LinearLayoutManager(context)
    adapter = recyclerViewAdapter
    itemAnimator = null
    autoScrollToStart(recyclerViewAdapter)
}
