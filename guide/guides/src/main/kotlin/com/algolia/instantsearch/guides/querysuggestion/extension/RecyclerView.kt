package com.algolia.instantsearch.guides.querysuggestion.extension

import android.content.Context
import android.view.View
import android.view.ViewGroup
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

internal fun createRecyclerView(context: Context) = RecyclerView(context).apply {
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
}
