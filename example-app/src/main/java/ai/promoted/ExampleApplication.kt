package ai.promoted

import android.app.Application

val USE_JAVA = BuildConfig.FLAVOR.equals("java", ignoreCase = true)

class ExampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Switch over to Java example if necessary
        if(USE_JAVA) return ExampleApplicationJavaDelegate().onCreate(this)

        PromotedAi.initialize(this) {
            metricsLoggingUrl = "https://5tbepnh11h.execute-api.us-east-2.amazonaws.com/dev/main"
            metricsLoggingApiKey = "OLpsrVSd565IQmOAR62dO9GkXUJngNo5ZUdCMV70"
        }
    }
}