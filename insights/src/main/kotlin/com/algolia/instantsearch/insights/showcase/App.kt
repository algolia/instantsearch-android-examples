package com.algolia.instantsearch.insights.showcase

import android.app.Application
import com.algolia.instantsearch.insights.registerInsights
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.insights.UserToken

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        registerInsights(this, AppID, ApiKey, IndexName).apply {
            loggingEnabled = true
            userToken = UserToken("userToken")
            minBatchSize = 1
        }.also {
            it.loggingEnabled = true
        }
    }

    companion object {
        val AppID = ApplicationID("latency")
        val ApiKey = APIKey("afc3dd66dd1293e2e2736a5a51b05c0a")
        val IndexName = IndexName("bestbuy")
    }
}
