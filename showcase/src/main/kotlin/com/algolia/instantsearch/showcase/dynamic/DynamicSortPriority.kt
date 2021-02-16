package com.algolia.instantsearch.showcase.dynamic

/**
 * Represents the priority to apply to the search in the dynamically sorted index
 *
 * @param relevancyStrictness relevancy strictness value to apply to the search
 */
public enum class DynamicSortPriority(
    public val relevancyStrictness: Int
) {

    /** Prioritize less more relevant results */
    Relevancy(100),

    /** Prioritize more less relevant results */
    HitsCount(0);

    companion object {
        fun of(relevancyStrictness: Int): DynamicSortPriority = when (relevancyStrictness) {
            0 -> HitsCount
            else -> Relevancy
        }
    }
}
