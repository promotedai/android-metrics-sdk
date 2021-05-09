package ai.promoted.metrics.usecases

import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.id.UuidGenerator
import ai.promoted.mockkRelaxedUnit
import ai.promoted.proto.event.View
import ai.promoted.xray.NoOpXray
import com.google.protobuf.Message
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TrackViewUseCaseTest {
    private val enqueuedMessages = mutableListOf<Message>()
    private val logger = mockkRelaxedUnit<MetricsLogger> {
        every { enqueueMessage(capture(enqueuedMessages)) } returns Unit
    }

    private val testTime = 100L
    private val testSessionId = "test-session-id"

    private val useCase = TrackViewUseCase(
        clock = mockk { every { currentTimeMillis } returns testTime },
        deviceInfoProvider = mockk(relaxed = true),
        logger = logger,
        idGenerator = UuidGenerator(),
        sessionUseCase = mockkRelaxedUnit {
            every { sessionId } returns testSessionId
        },
        xray = NoOpXray()
    )

    @Test
    fun `Pre-emptive view ID is preserved after first view becomes visible`() {
        // Given
        val viewIdBeforeViewVisible = useCase.viewId

        // When
        useCase.onViewVisible("view-id")

        // Then
        assertThat(
            useCase.viewId,
            equalTo(viewIdBeforeViewVisible)
        )
    }

    @Test
    fun `New view ID is generated after second view (a view with a key different than the first key used) becomes visible`() {
        // Given a view already became visible
        useCase.onViewVisible("view-id-1")
        val firstViewId = useCase.viewId

        // When a second view becomes visible
        useCase.onViewVisible("view-id-2")
        val secondViewId = useCase.viewId

        // Then
        assertThat(secondViewId, not(firstViewId))
    }

    @Test
    fun `View ID is not changed when the last visible view becomes visible again`() {
        // Given a view became visible once
        useCase.onViewVisible("view-id-1")
        val firstId = useCase.viewId

        // When it becomes visible a second time, without any other view having become visible
        useCase.onViewVisible("view-id-1")
        val secondId = useCase.viewId

        // Then
        assertThat(firstId, equalTo(secondId))
    }

    @Test
    fun `View is logged after start view becomes v isible`() {
        // When a view becomes visible
        useCase.onViewVisible("view-id")

        // Then
        verify(exactly = 1) {
            logger.enqueueMessage(ofType(View::class))
        }
    }
}