package ai.promoted

import ai.promoted.metrics.DefaultMetricsLogger
import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.NoOpMetricsLogger

private val defaultMetricsLoggerProvider: (config: ClientConfig) -> MetricsLogger
    get() = { config ->
        if (config.loggingEnabled) DefaultMetricsLogger(config)
        else NoOpMetricsLogger
    }

abstract class PromotedAi internal constructor(
    private val metricsLoggerProvider: (config: ClientConfig) -> MetricsLogger = defaultMetricsLoggerProvider
) {
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
        metricsLogger = if (config.loggingEnabled) metricsLoggerProvider.invoke(config)
        else NoOpMetricsLogger
    }

    // Default singleton instance provided for ease of API usage
    companion object : PromotedAi()
}