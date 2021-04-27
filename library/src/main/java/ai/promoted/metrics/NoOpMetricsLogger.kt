package ai.promoted.metrics

object NoOpMetricsLogger : MetricsLogger {
    override fun log() {

    }

    override fun shutdown() {

    }
}