package ai.promoted

import ai.promoted.metrics.DefaultMetricsLogger
import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.NoOpMetricsLogger

object Promoted {
    lateinit var metricsLogger: MetricsLogger
        private set

    fun start(block: ClientConfig.Builder.() -> Unit) {
        val configBuilder = ClientConfig.Builder()
        block(configBuilder)
        start(configBuilder.build())
    }

    fun reconfigure(block: ClientConfig.Builder.() -> Unit) {
        if (this::metricsLogger.isInitialized) metricsLogger.shutdown()
        start(block)
    }

    private fun start(config: ClientConfig) {
        metricsLogger = if (config.loggingEnabled) DefaultMetricsLogger(config)
        else NoOpMetricsLogger
    }
}