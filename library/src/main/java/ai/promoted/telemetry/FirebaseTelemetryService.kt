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
    override fun logEvent(name: String, params: Map<String, TelemetryService.ParamValue>) {
        analytics.logEvent(name) {
            params.forEach { entry ->
                when (val paramValue = entry.value) {
                    is TelemetryService.ParamValue.String -> param(entry.key, paramValue.rawValue)
                    is TelemetryService.ParamValue.Integer ->
                        param(entry.key, paramValue.rawValue.toLong())
                    is TelemetryService.ParamValue.Boolean ->
                        param(entry.key, paramValue.rawValue.toString())
                }
            }
        }
    }
}
