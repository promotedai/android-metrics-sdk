package ai.promoted.telemetry

/**
 * Represents some third party service that allows for logging events for telemetry purposes.
 */
internal interface TelemetryService {
    /**
     * Log an event with the given name and given parameters. Currently only supports parameter
     * values that are strings.
     */
    fun logEvent(name: String, params: Map<String, String> = emptyMap())

    /**
     * A specialized function for (where supported) incrementing the server-side-tracked sum of
     * some event's value, as opposed to just logging a single event w/ parameters.
     *
     * For example, if you want to keep a running sum of total bytes sent by an app, this function
     * is intended to allow you to add to that sum by the integer you provide.
     */
    fun addToEventValue(name: String, amountToAdd: Int)
}
