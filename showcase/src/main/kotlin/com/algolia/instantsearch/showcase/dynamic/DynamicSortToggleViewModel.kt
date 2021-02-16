package com.algolia.instantsearch.showcase.dynamic

import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.showcase.dynamic.DynamicSortPriority.HitsCount
import com.algolia.instantsearch.showcase.dynamic.DynamicSortPriority.Relevancy

/**
 * The component that stores the currently applied dynamic sort priority applied to the search in
 * the dynamically sorted index (virtual replica) and provides the interface to toggle this value.
 *
 * Usage of the dynamically sorted index introduces the trade-off between the number of results and
 * the relevancy of results. DynamicSortToggle components provide a convenient interface to switch
 * between these options.
 */
public class DynamicSortToggleViewModel(
    priority: DynamicSortPriority? = null
) {

    /**
     * The priority to apply to the search in the dynamically sorted index.
     *
     * `null` value represents the undefined state, meaning that either the view model has never
     * been connected to a searcher, or the searched index is not the virtual replica.
     */
    public val priority: SubscriptionValue<DynamicSortPriority?> = SubscriptionValue(priority)

    /**
     * Switch the dynamic sort priority to the opposite one.
     * Skipped if the current value of sort priority is `null`.
     */
    fun toggle() {
        when (priority.value) {
            Relevancy -> priority.value = HitsCount
            HitsCount -> priority.value = Relevancy
        }
    }
}
