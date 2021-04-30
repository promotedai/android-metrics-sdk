package ai.promoted

import android.app.Application

class ExampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        PromotedAi.configure(this) {
            metricsLoggingUrl = "https://5tbepnh11h.execute-api.us-east-2.amazonaws.com/dev/main"
            metricsLoggingAPIKey = "OLpsrVSd565IQmOAR62dO9GkXUJngNo5ZUdCMV70"
        }
    }
}