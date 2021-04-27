package ai.promoted

import ai.promoted.metrics.DefaultMetricsLogger
import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.NoOpMetricsLogger
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertThat
import org.junit.Assert.fail
import org.junit.Test

class PromotedAiTest {
    private var metricsLoggerProvider: (config: ClientConfig) -> MetricsLogger = {
        DefaultMetricsLogger(it)
    }

    private val promotedAi = object : PromotedAi(metricsLoggerProvider) {}

    @Test
    fun `Throws error if start() or reconfigure() not called`() {
        try {
            promotedAi.metricsLogger.log()
            fail("Cannot have a logger without start() being called")
        } catch (error: Exception) {
            // success
            error.printStackTrace()
        }
    }

    @Test
    fun `Metrics logger changes to no-op when reconfigured to disable logging`() {
        // Given
        promotedAi.start { loggingEnabled = true }
        assertThat(promotedAi.metricsLogger, instanceOf(DefaultMetricsLogger::class.java))

        // When
        promotedAi.reconfigure { loggingEnabled = false }

        // Then
        assertThat(promotedAi.metricsLogger, instanceOf(NoOpMetricsLogger::class.java))
    }

    @Test
    fun `Existing metrics logger is shut down upon reconfigure`() {
        // Given
        promotedAi.start { loggingEnabled = true}
        val initialMetricsLogger = promotedAi.metricsLogger

        // When
        promotedAi.reconfigure { }
        val reconfiguredMetricsLogger = promotedAi.metricsLogger

        // Then
        assertThat(reconfiguredMetricsLogger, not(initialMetricsLogger))
    }
}