package com.algolia.instantsearch.insights.showcase

import android.app.Application
import com.algolia.instantsearch.insights.Insights
import com.algolia.instantsearch.insights.register
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.insights.UserToken

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Insights.register(this, APP_ID, API_KEY, INDEX_NAME).apply {
            loggingEnabled = true
            userToken = UserToken("userToken")
            minBatchSize = 1
        }.also {
            it.loggingEnabled = true
        }
    }

    companion object {
        val APP_ID = ApplicationID("latency")
        val API_KEY = APIKey("afc3dd66dd1293e2e2736a5a51b05c0a")
        val INDEX_NAME = IndexName("bestbuy")
    }
}
