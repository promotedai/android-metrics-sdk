package ai.promoted.telemetry

// All functions here will be empty
@Suppress("EmptyFunctionBlock")
internal class NoOpTelemetry : Telemetry {
    override fun onMetricsSent(countSent: Int, bytesSent: Int) {

    }

    override fun onError(error: Throwable) {
    }
}
