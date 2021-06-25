package ai.promoted.telemetry

import android.content.Context

private const val EVENT_METRICS_SENT = "metrics-sent"
private const val PARAM_NUMBER_SENT = "number-sent"
private const val PARAM_BYTES_SENT = "bytes-sent"
private const val EVENT_ERROR_OCCURRED = "error"
private const val PARAM_ERROR_MESSAGE = "error-message"
private const val PARAM_ERROR_TYPE = "error-type"

/**
 * This class provides a way to track the performance and behavior of the Promoted SDK. It will
 * use [TelemetryServiceFinder] to find the best available telemetry service (i.e. Firebase), based
 * on what third party SDKs the library user has decided to include in their application, and then
 * use that service to log performance/behavioral events about the Promoted SDK.
 */
internal class Telemetry(
    context: Context?,
    telemetryServiceFinder: TelemetryServiceFinder
) {
    private val service: TelemetryService =
        telemetryServiceFinder.findAvailableService(context)

    /**
     * To be called whenever a new batch of metrics has been sent to the Promoted server.
     */
    fun onMetricsSent(countSent: Int, bytesSent: Int) {
        service.logEvent(
            EVENT_METRICS_SENT,
            mapOf(
                PARAM_NUMBER_SENT to TelemetryService.ParamValue.Integer(countSent),
                PARAM_BYTES_SENT to TelemetryService.ParamValue.Integer(bytesSent)
            )
        )
    }

    /**
     * To be called whenever an error (fatal or non-fatal) has occurred that has originated in the
     * Promoted SDK's classes
     */
    fun onError(message: String, error: Throwable) {
        service.logEvent(
            EVENT_ERROR_OCCURRED,
            mapOf(
                PARAM_ERROR_MESSAGE to TelemetryService.ParamValue.String(message),
                PARAM_ERROR_TYPE to TelemetryService.ParamValue.String(
                    error::class.qualifiedName ?: "AnonymousClass"
                )
            )
        )
    }
}
