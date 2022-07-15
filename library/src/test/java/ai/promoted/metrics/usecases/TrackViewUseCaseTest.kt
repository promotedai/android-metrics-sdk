package ai.promoted.metrics.usecases

import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.id.AncestorId
import ai.promoted.metrics.id.UuidGenerator
import ai.promoted.mockkRelaxedUnit
import ai.promoted.proto.event.AutoView
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
        logger = logger,
        clock = mockk { every { currentTimeMillis } returns testTime },
        deviceInfoProvider = mockk(relaxed = true),
        idGenerator = UuidGenerator(),
        sessionUseCase = mockkRelaxedUnit {
            every { sessionId } returns testSessionId
        },
        xray = NoOpXray()
    )

    @Test
    fun `Pre-emptive auto-view ID is preserved after first view becomes visible`() {
        // Given
        val viewIdBeforeViewVisible = useCase.autoViewId.currentOrPendingValue

        // When
        useCase.onImplicitViewVisible("view-key")

        // Then
        assertThat(
            useCase.autoViewId.currentValue,
            equalTo(viewIdBeforeViewVisible)
        )
    }

    @Test
    fun `New auto-view ID is generated after a different view becomes visible`() {
        // Given a view already became visible
        useCase.onImplicitViewVisible("view-id-1")
        val firstViewId = useCase.autoViewId.currentValue

        // When a second view becomes visible
        useCase.onImplicitViewVisible("view-id-2")
        val secondViewId = useCase.autoViewId.currentValue

        // Then
        assertThat(secondViewId, not(firstViewId))
    }

    @Test
    fun `Auto-view ID is not changed when the last visible view becomes visible again`() {
        // Given a view became visible once
        useCase.onImplicitViewVisible("view-id-1")
        val firstId = useCase.autoViewId.currentValue

        // When it becomes visible a second time, without any other view having become visible
        useCase.onImplicitViewVisible("view-id-1")
        val secondId = useCase.autoViewId.currentValue

        // Then
        assertThat(firstId, equalTo(secondId))
    }

    @Test
    fun `AutoView is logged after start view becomes visible`() {
        // When a view becomes visible
        useCase.onImplicitViewVisible("view-key")

        // Then
        verify(exactly = 1) {
            logger.enqueueMessage(ofType(AutoView::class))
        }
    }

    @Test
    fun `View is logged after logView`() {
        // When logView is called
        useCase.logView("the-view-id")

        // Then
        verify(exactly = 1) {
            logger.enqueueMessage(ofType(View::class))
        }
    }

    @Test
    fun `AutoView is logged after logAutoView`() {
        // When logAutoView is called
        useCase.logAutoView("the-view-id", "", "")

        // Then
        verify(exactly = 1) {
            logger.enqueueMessage(ofType(AutoView::class))
        }
    }
}
