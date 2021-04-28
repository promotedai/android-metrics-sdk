package ai.promoted

import android.app.Application

class ExampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        PromotedAi.start {
            metricsLoggingUrl = ""
            metricsLoggingAPIKey = ""
        }
    }
}