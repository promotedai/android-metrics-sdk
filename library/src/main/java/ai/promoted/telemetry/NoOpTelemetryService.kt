package ai.promoted.telemetry

/**
 * A class to be used in the case where there are no available [TelemetryService]s.
 */
// All functions here will be empty
@Suppress("EmptyFunctionBlock")
internal class NoOpTelemetryService : TelemetryService {
    override fun logEvent(name: String, params: Map<String, TelemetryService.ParamValue>) {
    }
}
