package com.algolia.instantsearch.showcase.dynamic.components

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.ConnectionImpl

internal class DynamicSortToggleConnectionView(
    private val viewModel: DynamicSortViewModel,
    private val view: DynamicSortView,
) : ConnectionImpl() {

    val callback: Callback<DynamicSortPriority?> = {
        view.currentPriority(it)
    }

    override fun connect() {
        super.connect()
        view.didToggle = { viewModel.toggle() }
        viewModel.priority.subscribePast(callback)
    }

    override fun disconnect() {
        super.disconnect()
        view.didToggle = null
        viewModel.priority.unsubscribe(callback)
    }
}

/**
 * Create a connection between a view to the dynamic sort components.
 *
 * @param view the view that will render dynamic sort toggle
 */
fun DynamicSortViewModel.connectView(view: DynamicSortView): Connection {
    return DynamicSortToggleConnectionView(this, view)
}
