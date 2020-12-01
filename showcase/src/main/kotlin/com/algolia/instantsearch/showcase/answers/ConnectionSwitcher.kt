package com.algolia.instantsearch.showcase.answers

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.showcase.answers.SearchMode.HYBRID
import com.algolia.instantsearch.showcase.answers.SearchMode.QA
import com.algolia.instantsearch.showcase.answers.SearchMode.STANDARD

class ConnectionSwitcher(
    private val searchHits: Connection,
    private val answersHits: Connection,
    private val answersView: Connection
) {

    fun switchTo(mode: SearchMode) {
        when (mode) {
            HYBRID -> {
                searchHits.connect()
                answersHits.disconnect()
                answersView.connect()
            }
            STANDARD -> {
                searchHits.connect()
                answersHits.disconnect()
                answersView.disconnect()
            }
            QA -> {
                searchHits.disconnect()
                answersHits.connect()
                answersView.disconnect()
            }
        }
    }
}
