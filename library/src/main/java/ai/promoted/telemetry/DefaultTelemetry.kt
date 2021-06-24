package ai.promoted.telemetry

import android.annotation.SuppressLint
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.crashlytics.FirebaseCrashlytics

private const val EVENT_METRICS_SENT = "metrics-sent"
private const val PARAM_NUMBER_SENT = "number-sent"
private const val PARAM_BYTES_SENT = "bytes-sent"
private const val EVENT_ERROR_OCCURRED = "error"
private const val PARAM_ERROR_MESSAGE = "error-message"
private const val PARAM_ERROR_TYPE = "error-type"

internal class DefaultTelemetry private constructor(
    private val analytics: FirebaseAnalytics,
    private val crashlytics: FirebaseCrashlytics?
) : Telemetry {
    override fun onMetricsSent(countSent: Int, bytesSent: Int) {
        crashlytics?.log("[PROMOTED] - Sent $countSent events / $bytesSent bytes")
        analytics.logEvent(EVENT_METRICS_SENT) {
            param(PARAM_NUMBER_SENT, countSent.toLong())
            param(PARAM_BYTES_SENT, bytesSent.toLong())
        }
    }

    override fun onError(message: String, error: Throwable) {
        crashlytics?.log(message)
        crashlytics?.recordException(error)
        analytics.logEvent(EVENT_ERROR_OCCURRED) {
            param(PARAM_ERROR_MESSAGE, message)
            param(PARAM_ERROR_TYPE, error::class.qualifiedName ?: "AnonymousClass")
        }
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
            val hasAnalytics = getAnalyticsClass() != null
            if(!hasAnalytics) return null

            // Crashlytics is optional
            val hasCrashlytics = getCrashlyticsClass() != null

            val analytics = FirebaseAnalytics.getInstance(context)
            val crashlytics = if(hasCrashlytics) FirebaseCrashlytics.getInstance() else null

            return DefaultTelemetry(analytics, crashlytics)
        }

        // Suppressing this Detekt check because if we run into *any* issues accessing the Firebase
        // classes, we cannot safely assume they are available.
        @Suppress("TooGenericExceptionCaught", "SwallowedException")
        private fun getAnalyticsClass(): Class<*>? {
            return try {
                Class.forName("com.google.firebase.analytics.FirebaseAnalytics")
            } catch (error: Throwable) {
                null
            }
        }

        // Suppressing this Detekt check because if we run into *any* issues accessing the Firebase
        // classes, we cannot safely assume they are available.
        @Suppress("TooGenericExceptionCaught", "SwallowedException")
        private fun getCrashlyticsClass(): Class<*>? {
            return try {
                Class.forName("com.google.firebase.crashlytics.FirebaseCrashlytics")
            } catch (error: Throwable) {
                null
            }
        }
    }
}
