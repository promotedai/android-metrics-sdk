package ai.promoted.telemetry

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * An implementation of [TelemetryService] that passes through to [FirebaseAnalytics]
 */
internal class FirebaseTelemetryService(
    private val analytics: FirebaseAnalytics
) : TelemetryService {
    /**
     * Log an event to [FirebaseAnalytics], mapping the [TelemetryService.ParamValue] to Firebase's
     * supported parameter types.
     *
     * @see [TelemetryService.logEvent]
     */
    override fun logEvent(name: String, params: Map<String, String>) {
        val paramsBundle = Bundle()
        params.forEach { entry ->
            paramsBundle.putString(entry.key, entry.value)
        }

        analytics.logEvent(name, paramsBundle)
    }

    /**
     * Increment the [FirebaseAnalytics.Param.VALUE] of the event with the given name, by the given
     * amount to add.
     *
     * @see [TelemetryService.addToEventValue]
     */
    override fun addToEventValue(name: String, amountToAdd: Int) {
        val paramsBundle = Bundle()
        paramsBundle.putLong(FirebaseAnalytics.Param.VALUE, amountToAdd.toLong())
        analytics.logEvent(name, paramsBundle)
    }
}
