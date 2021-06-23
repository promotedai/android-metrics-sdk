package ai.promoted.telemetry

import android.annotation.SuppressLint
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics

internal class DefaultTelemetry(
    private val analytics: FirebaseAnalytics,
    private val crashlytics: FirebaseCrashlytics
) : Telemetry {
    override fun onMetricsSent(countSent: Int, bytesSent: Int) {
        crashlytics.log("[PROMOTED] - Sent $countSent events / $bytesSent bytes")

        // TODO - increment event count
        // TODO - increment byte count
        // analytics.logEvent(...)
    }

    override fun onError(error: Throwable) {
        crashlytics.recordException(error)
    }

    companion object {
        /**
         * Executes a run-time check of whether the required Firebase classes are available on the
         * classpath. If not, then we cannot do telemetry, so a no-op must be used.
         *
         * If the required classes are available, an instance of [DefaultTelemetry] is returned;
         * otherwise, null is returned.
         */
        // Suppressing this Lint message because it will be up to library users to have pulled in
        // Firebase and declared the necessary permissions
        @SuppressLint("MissingPermission")
        fun createInstanceIfAvailable(context: Context): DefaultTelemetry? {
            return try {
                Class.forName("com.google.firebase.analytics.FirebaseAnalytics")
                Class.forName("com.google.firebase.crashlytics.FirebaseCrashlytics")
                DefaultTelemetry(
                    FirebaseAnalytics.getInstance(context),
                    FirebaseCrashlytics.getInstance()
                )
            } catch (error: Throwable) {
                null
            }
        }
    }
}