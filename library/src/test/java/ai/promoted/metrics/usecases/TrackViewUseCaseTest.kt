package ai.promoted.metrics.usecases

import ai.promoted.SystemOutLogger
import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.id.AncestorId
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
    private val testSessionId =
        AncestorId(UuidGenerator()).apply {
            override("test-session-id")
        }

    private val useCase = TrackViewUseCase(
        systemLogger = SystemOutLogger(),
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
        val viewIdBeforeViewVisible = useCase.viewId.currentOrPendingValue

        // When
        useCase.onViewVisible("view-key")

        // Then
        assertThat(
            useCase.viewId.currentValue,
            equalTo(viewIdBeforeViewVisible)
        )
    }

    @Test
    fun `New view ID is generated after second view (a view with a key different than the first key used) becomes visible`() {
        // Given a view already became visible
        useCase.onViewVisible("view-id-1")
        val firstViewId = useCase.viewId.currentValue

        // When a second view becomes visible
        useCase.onViewVisible("view-id-2")
        val secondViewId = useCase.viewId.currentValue

        // Then
        assertThat(secondViewId, not(firstViewId))
    }

    @Test
    fun `View ID is not changed when the last visible view becomes visible again`() {
        // Given a view became visible once
        useCase.onViewVisible("view-id-1")
        val firstId = useCase.viewId.currentValue

        // When it becomes visible a second time, without any other view having become visible
        useCase.onViewVisible("view-id-1")
        val secondId = useCase.viewId.currentValue

        // Then
        assertThat(firstId, equalTo(secondId))
    }

    @Test
    fun `View is logged after start view becomes visible`() {
        // When a view becomes visible
        useCase.onViewVisible("view-key")

        // Then
        verify(exactly = 1) {
            logger.enqueueMessage(ofType(View::class))
        }
    }

    @Test
    fun `View ID is logged after logView`() {
        // When logView is called
        useCase.logView("the-view-id")

        // Then
        verify(exactly = 1) {
            logger.enqueueMessage(ofType(View::class))
        }
    }
}