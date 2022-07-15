package ai.promoted

import ai.promoted.http.RetrofitNetworkConnection
import ai.promoted.platform.AppRuntimeEnvironment
import android.os.Debug
import io.mockk.*
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

/**
 * Simple tests to verify defaults on client config
 */
class ClientConfigTest {
    // Ensures AppRuntimeEnvironment doesn't actually attempt to invoke Android SDK
    @Before
    fun setup() {
        mockkObject(AppRuntimeEnvironment.Companion)
        every { AppRuntimeEnvironment.Companion.default } returns FakeAppRuntimeEnvironment(
            isDebuggable = true
        )
    }

    @After
    fun tearDown() {
        unmockkObject(AppRuntimeEnvironment.Companion)
    }

    @Test
    fun `Client config baseline default`() {
        // Given we're in a standard prod environment
        every { AppRuntimeEnvironment.Companion.default } returns FakeAppRuntimeEnvironment(
            isDebuggable = false
        )

        // When
        val config = ClientConfig.Builder().build()

        // Then
        assertThat(config.loggingEnabled, equalTo(true))
        assertThat(config.loggingAnomalyHandling, equalTo(ClientConfig.LoggingAnomalyHandling.None))
        assertThat(
            config.metricsLoggingWireFormat,
            equalTo(ClientConfig.MetricsLoggingWireFormat.Binary)
        )
        assertThat(config.loggingFlushIntervalSeconds, equalTo(10))
        assertThat(
            config.networkConnectionProvider.invoke(),
            instanceOf(RetrofitNetworkConnection::class.java)
        )
    }

    @Test
    fun `Client config prefers modal logging anomaly when debuggable build`() {
        // Given we're in a standard prod environment
        every { AppRuntimeEnvironment.Companion.default } returns FakeAppRuntimeEnvironment(
            isDebuggable = true
        )

        // When
        val config = ClientConfig.Builder().build()

        // Then
        assertThat(
            config.loggingAnomalyHandling,
            equalTo(ClientConfig.LoggingAnomalyHandling.ModalDialog)
        )
    }
}
