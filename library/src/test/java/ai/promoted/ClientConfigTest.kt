package ai.promoted

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test

/**
 * Simple tests to verify defaults on client config
 */
class ClientConfigTest {
    @Test
    fun `Client config baseline default`() {
        val config = ClientConfig.Builder().build()

        assertThat(config.loggingEnabled, equalTo(true))
        assertThat(config.metricsLoggingWireFormat, equalTo(ClientConfig.MetricsLoggingWireFormat.Binary))
        assertThat(config.loggingFlushInterval, equalTo(10))
    }
}