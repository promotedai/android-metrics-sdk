package ai.promoted

import ai.promoted.metrics.DefaultMetricsLogger
import ai.promoted.metrics.NoOpMetricsLogger
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertThat
import org.junit.Assert.fail
import org.junit.Test

class PromotedAiTest {
    @Test
    fun `Throws error if start() or reconfigure() not called`() {
        try {
            PromotedAi.metricsLogger.log()
            fail("Cannot have a logger without start() being called")
        } catch (error: Throwable) {
            // success
            error.printStackTrace()
        }
    }

    @Test
    fun `Metrics logger changes to no-op when reconfigured to disable logging`() {
        // Given
        PromotedAi.start { loggingEnabled = true }
        assertThat(PromotedAi.metricsLogger, instanceOf(DefaultMetricsLogger::class.java))

        // When
        PromotedAi.reconfigure { loggingEnabled = false }

        // Then
        assertThat(PromotedAi.metricsLogger, instanceOf(NoOpMetricsLogger::class.java))
    }

    @Test
    fun `Existing metrics logger is shut down upon reconfigure`() {
        // TODO
    }
}