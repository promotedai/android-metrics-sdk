package ai.promoted

import ai.promoted.metrics.DefaultMetricsLogger
import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.NoOpMetricsLogger
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.assertThat
import org.junit.Assert.fail
import org.junit.Test

class PromotedAiTest {
    private val metricsLoggerProvider: (config: ClientConfig) -> MetricsLogger = {
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
    fun `Existing metrics logger is shut down and re-set upon reconfigure`() {
        // Given a PromotedAi instance that starts with an observable metrics logger
        val observableMetricsLogger = object : MetricsLogger {
            var didShutdown = false
            override fun log() {}

            override fun shutdown() {
                didShutdown = true
            }
        }

        val promotedAi = object : PromotedAi({
            if(!observableMetricsLogger.didShutdown) observableMetricsLogger
            else DefaultMetricsLogger(it)
        }) {}

        promotedAi.start { loggingEnabled = true }

        // When reconfigured
        promotedAi.reconfigure { }
        val reconfiguredMetricsLogger = promotedAi.metricsLogger

        // Then shutdown was called on the first metrics logger
        // And another metrics logger was instantiated
        assertThat(observableMetricsLogger.didShutdown, equalTo(true))
        assertThat(reconfiguredMetricsLogger, not(observableMetricsLogger))
    }
}