package com.algolia.instantsearch.insights.extension

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.algolia.instantsearch.helper.android.list.autoScrollToStart
import com.algolia.instantsearch.insights.ListItemAdapter
import kotlinx.android.synthetic.main.demo_activity.*

fun AppCompatActivity.configureRecyclerView(adapter: ListItemAdapter) {
    recyclerView.let {
        it.visibility = View.VISIBLE
        it.layoutManager = LinearLayoutManager(this)
        it.adapter = adapter
        it.itemAnimator = null
        it.autoScrollToStart(adapter)
    }
}
