package ai.promoted.metrics.usecases

import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.id.AncestorId
import ai.promoted.metrics.id.IdGenerator
import ai.promoted.metrics.id.UuidGenerator
import ai.promoted.proto.common.Timing
import ai.promoted.proto.event.Action
import ai.promoted.proto.event.ActionType
import ai.promoted.xray.NoOpXray
import com.google.protobuf.Message
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TrackActionUseCaseTest {
    private val randomUuid = "random-uuid"
    private val testSessionId =
        AncestorId(UuidGenerator()).apply {
            override("session-id")
        }

    private val testAutoViewId = AncestorId(UuidGenerator()).apply {
        override("auto-view-id")
    }

    private val enqueuedMessage = CapturingSlot<Message>()
    private val basedOnUuid = CapturingSlot<String>()
    private val logger = mockk<MetricsLogger> {
        every { enqueueMessage(capture(enqueuedMessage)) } returns Unit
    }
    private val idGenerator = mockk<IdGenerator> {
        every { newId() } returns randomUuid
        every { newId(basedOn = capture(basedOnUuid)) } answers {
            firstArg()
        }
    }

    private val useCase = TrackActionUseCase(
        clock = mockk { every { currentTimeMillis } returns 0L },
        logger = logger,
        idGenerator = idGenerator,
        sessionUseCase = mockk {
            every { sessionId } returns testSessionId
        },
        viewUseCase = mockk {
            every { autoViewId } returns testAutoViewId
        },
        NoOpXray()
    )

    @Test
    fun `Simple action is logged when no data block`() {
        // When
        useCase.onAction(null, "test", ActionType.ADD_TO_CART, dataBlock = null)

        // Then
        val action = enqueuedMessage.captured as? Action
        verify(exactly = 1) { logger.enqueueMessage(any()) }
        assertThat(action, notNullValue())
    }

    @Test
    fun `Impression ID is null or empty if both insertion ID and content ID are null`() {
        // When
        useCase.onAction(null, "test", ActionType.CUSTOM_ACTION_TYPE) {
            insertionId = null
            contentId = null
            requestId = "test-request-id"
            elementId = "test-element-id"
            targetUrl = null

            // Fake custom properties
            customProperties = Timing.newBuilder().setClientLogTimestamp(0L).build()
        }

        // Then
        val action = enqueuedMessage.captured as? Action
        verify(exactly = 1) { logger.enqueueMessage(any()) }
        assertThat(action, notNullValue())
        assertThat(action?.impressionId ?: "", equalTo(""))
    }

    @Test
    fun `Correct session ID and view ID are used in message`() {
        // When
        useCase.onAction(null, "test", ActionType.CUSTOM_ACTION_TYPE) {
            insertionId = null
            contentId = null
            requestId = "test-request-id"
            elementId = "test-element-id"
            targetUrl = null

            // Fake custom properties
            customProperties = Timing.newBuilder().setClientLogTimestamp(0L).build()
        }

        // Then
        val action = enqueuedMessage.captured as? Action
        verify(exactly = 1) { logger.enqueueMessage(any()) }
        assertThat(action, notNullValue())
        assertThat(action?.sessionId, equalTo(testSessionId.currentValue))
        assertThat(action?.autoViewId, equalTo(testAutoViewId.currentValue))
    }
}
