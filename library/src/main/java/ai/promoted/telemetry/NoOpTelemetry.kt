package ai.promoted.telemetry

internal class NoOpTelemetry : Telemetry {
    override fun onMetricsSent(countSent: Int, bytesSent: Int) {

    }

    override fun onError(error: Throwable) {
    }
}