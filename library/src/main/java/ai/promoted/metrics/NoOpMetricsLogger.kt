package ai.promoted.metrics

internal object NoOpMetricsLogger : MetricsLogger {
    override fun log() {

    }

    override fun shutdown() {

    }
}