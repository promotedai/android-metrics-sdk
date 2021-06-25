package ai.promoted.telemetry

/**
 * Represents some third party service that allows for logging events for telemetry purposes.
 */
internal interface TelemetryService {
    /**
     * A sealed type structure of supported parameter value types. This is to allow for
     * service-generic providing of event parameters.
     */
    sealed class ParamValue {
        data class Boolean(val rawValue: kotlin.Boolean) : ParamValue()
        data class Integer(val rawValue: Int) : ParamValue()
        data class String(val rawValue: kotlin.String) : ParamValue()
    }

    /**
     * Log an event with the given name and given parameters
     */
    fun logEvent(name: String, params: Map<String, ParamValue> = emptyMap())
}
