package ai.promoted.test

import ai.promoted.metrics.NoOpMetricsLogger

internal class ObservableNoOpMetricsLogger : NoOpMetricsLogger() {
    var didShutdown = false

    override fun shutdown() {
        didShutdown = true
    }
}