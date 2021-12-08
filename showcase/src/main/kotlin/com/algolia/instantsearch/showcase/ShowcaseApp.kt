package com.algolia.instantsearch.showcase

import android.app.Application
import com.algolia.instantsearch.core.telemetry.SharedTelemetry

class ShowcaseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        SharedTelemetry.enabled = false
    }
}
