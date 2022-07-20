package ai.promoted

import android.app.Application

class ExampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        PromotedAi.initialize(this) {
            metricsLoggingUrl = "https://5tbepnh11h.execute-api.us-east-2.amazonaws.com/dev/main"
            metricsLoggingApiKey = "OLpsrVSd565IQmOAR62dO9GkXUJngNo5ZUdCMV70"
            xrayEnabled = true
            loggingAnomalyContactInfo = ClientConfig.LoggingAnomalyContactInfo(
                slack = ClientConfig.LoggingAnomalyContactInfo.Slack("#snackpass-promoted"),
                email = ClientConfig.LoggingAnomalyContactInfo.Email(
                    "help+snackpass@promoted.ai"
                )
            )
        }
    }
}