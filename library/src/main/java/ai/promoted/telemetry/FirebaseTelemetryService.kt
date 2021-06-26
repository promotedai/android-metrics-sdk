package ai.promoted.telemetry

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent

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
        analytics.logEvent(name) {
            params.forEach { entry ->
                param(entry.key, entry.value)
            }
        }
    }

    /**
     * Increment the [FirebaseAnalytics.Param.VALUE] of the event with the given name, by the given
     * amount to add.
     *
     * @see [TelemetryService.addToEventValue]
     */
    override fun addToEventValue(name: String, amountToAdd: Int) {
        analytics.logEvent(name) {
            param(FirebaseAnalytics.Param.VALUE, amountToAdd.toLong())
        }
    }
}
