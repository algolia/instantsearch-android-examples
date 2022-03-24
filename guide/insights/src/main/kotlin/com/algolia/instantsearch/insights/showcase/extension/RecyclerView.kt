package com.algolia.instantsearch.insights.showcase.extension

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.android.list.autoScrollToStart
import com.algolia.instantsearch.insights.showcase.ListItemAdapter

fun AppCompatActivity.configureRecyclerView(recyclerView: RecyclerView, adapter: ListItemAdapter) {
    recyclerView.let {
        it.visibility = View.VISIBLE
        it.layoutManager = LinearLayoutManager(this)
        it.adapter = adapter
        it.itemAnimator = null
        it.autoScrollToStart(adapter)
    }
}
