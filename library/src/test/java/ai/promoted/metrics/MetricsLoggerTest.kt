package ai.promoted.metrics

import ai.promoted.NetworkConnection
import ai.promoted.PromotedApiRequest
import ai.promoted.metrics.usecases.FinalizeLogsUseCase
import ai.promoted.mockkRelaxedUnit
import ai.promoted.proto.event.Device
import ai.promoted.proto.event.User
import ai.promoted.telemetry.Telemetry
import ai.promoted.xray.NoOpXray
import com.google.protobuf.Message
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class MetricsLoggerTest {
    private val connection = mockkRelaxedUnit<NetworkConnection>()

    private val finalizedMessages = CapturingSlot<List<Message>>()

    private val testBodyDataSize = 100
    private val mockApiRequest = PromotedApiRequest(
        "",
        emptyMap(),
        ByteArray(testBodyDataSize)
    )
    private val finalizeUseCase = mockkRelaxedUnit<FinalizeLogsUseCase> {
        every { finalizeLogs(capture(finalizedMessages)) } returns mockApiRequest
    }

    private val mockTelemetry = mockkRelaxedUnit<Telemetry>()

    private val testFlushIntervalMillis = 500L
    private val logger = MetricsLogger(
        testFlushIntervalMillis,
        connection,
        finalizeUseCase,
        NoOpXray(),
        mockTelemetry
    )

    @Test
    fun `Messages always start off empty`() {
        logger.cancelAndSendPendingQueue()
        verify(exactly = 1) {
            finalizeUseCase.finalizeLogs(emptyList())
        }
    }

    @Test
    fun `Attempts to send over network after finalization`() = runBlocking {
        // Given
        val userMessage = User.newBuilder().build()
        val deviceMessage = Device.newBuilder().build()
        logger.enqueueMessage(userMessage)
        logger.enqueueMessage(deviceMessage)

        // When the interval passes (add a padding of time for the actual finalize function to
        // execute)
        delay(testFlushIntervalMillis + 100)

        // Then the messages were included in finalization
        coVerify(exactly = 1) { connection.send(mockApiRequest) }
    }

    @Test
    fun `All enqueued messages are included in finalization when time passes`() = runBlocking {
        // Given
        val userMessage = User.newBuilder().build()
        val deviceMessage = Device.newBuilder().build()
        logger.enqueueMessage(userMessage)
        logger.enqueueMessage(deviceMessage)

        // When the interval passes (add a padding of time for the actual finalize function to
        // execute)
        delay(testFlushIntervalMillis + 100)

        // Then the messages were included in finalization
        assertThat(finalizedMessages.captured.size, equalTo(2))
        assertThat(finalizedMessages.captured.contains(userMessage), equalTo(true))
        assertThat(finalizedMessages.captured.contains(deviceMessage), equalTo(true))
    }

    @Test
    fun `All enqueued messages are included in finalization when cancel and send`() = runBlocking {
        // Given
        val userMessage = User.newBuilder().build()
        val deviceMessage = Device.newBuilder().build()
        logger.enqueueMessage(userMessage)
        logger.enqueueMessage(deviceMessage)

        // When cancel and send after some pre-interval time
        delay(100L)
        logger.cancelAndSendPendingQueue()

        // Then the messages were included in finalization
        verify(exactly = 1) { finalizeUseCase.finalizeLogs(any()) }
        assertThat(finalizedMessages.captured.size, equalTo(2))
        assertThat(finalizedMessages.captured.contains(userMessage), equalTo(true))
        assertThat(finalizedMessages.captured.contains(deviceMessage), equalTo(true))
    }

    @Test
    fun `Pending finalization does not occur when cancel and discard`() = runBlocking {
        // Given
        val userMessage = User.newBuilder().build()
        val deviceMessage = Device.newBuilder().build()
        logger.enqueueMessage(userMessage)
        logger.enqueueMessage(deviceMessage)
        logger.cancelAndDiscardPendingQueue()

        // When the interval passes (add a padding of time for the actual finalize function to
        // execute)
        delay(testFlushIntervalMillis + 100)

        // Then the messages were included in finalization
        verify(exactly = 0) { finalizeUseCase.finalizeLogs(any()) }
    }

    @Test
    fun `Messages that were discarded do not occur in next finalization`() = runBlocking {
        // Given
        val userMessage = User.newBuilder().build()
        val deviceMessage = Device.newBuilder().build()
        logger.enqueueMessage(userMessage)
        logger.enqueueMessage(deviceMessage)
        logger.cancelAndDiscardPendingQueue()
        delay(testFlushIntervalMillis + 100)

        // When
        val userMessage2 = User.newBuilder().build()
        logger.enqueueMessage(userMessage2)
        delay(testFlushIntervalMillis + 100)

        // Then the messages were included in finalization
        verify(exactly = 1) { finalizeUseCase.finalizeLogs(any()) }
        assertThat(finalizedMessages.captured.size, equalTo(1))
        assertThat(finalizedMessages.captured.first(), equalTo(userMessage2))
    }

    @Test
    fun `Telemetry recorded after successful send`() = runBlocking {
        // Given metrics were logged
        val userMessage = User.newBuilder().build()
        val deviceMessage = Device.newBuilder().build()
        logger.enqueueMessage(userMessage)
        logger.enqueueMessage(deviceMessage)

        // When the batch was successfully sent
        delay(testFlushIntervalMillis + 100)

        // Then telemetry was notified
        verify(exactly = 1) {
            mockTelemetry.onMetricsSent(2, mockApiRequest.bodyData.size)
        }
    }

    @Test
    // Xray will globally handle passing any errors to Telemetry
    fun `Telemetry not recorded after failed send`() = runBlocking {
        // Given metrics were logged
        val userMessage = User.newBuilder().build()
        val deviceMessage = Device.newBuilder().build()
        logger.enqueueMessage(userMessage)
        logger.enqueueMessage(deviceMessage)
        // and the connection will fail
        coEvery { connection.send(any()) } throws Exception("Test failure")

        // When we attempted to send the batch
        delay(testFlushIntervalMillis + 100)

        // Then telemetry was not notified
        verify(exactly = 0) {
            mockTelemetry.onMetricsSent(any(), any())
        }
    }
}