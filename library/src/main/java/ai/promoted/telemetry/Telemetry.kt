package ai.promoted.telemetry

internal interface Telemetry {
    fun onMetricsSent(countSent: Int, bytesSent: Int)
    fun onError(error: Throwable)
}
