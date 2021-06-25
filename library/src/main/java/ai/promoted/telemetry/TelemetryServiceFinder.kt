package ai.promoted.telemetry

import android.annotation.SuppressLint
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Executes a run-time check of whether usable telemetry-service classes are available on the
 * classpath. If not, then we cannot do telemetry, so a no-op must be used.
 */
internal class TelemetryServiceFinder(private val classFinder: ClassFinder) {
    // Suppressing this Lint message because it will be up to library users to have pulled in
    // Firebase and declared the necessary permissions
    @SuppressLint("MissingPermission")
    fun findAvailableService(context: Context?): TelemetryService = when {
        hasFirebaseAnalyticsClass() && context != null ->
            FirebaseTelemetryService(FirebaseAnalytics.getInstance(context))
        else -> NoOpTelemetryService()
    }

    private fun hasFirebaseAnalyticsClass(): Boolean =
        classFinder.exists("com.google.firebase.analytics.FirebaseAnalytics")
}
