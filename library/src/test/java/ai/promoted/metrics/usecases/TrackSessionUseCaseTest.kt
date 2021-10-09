package ai.promoted.metrics.usecases

import ai.promoted.SystemOutLogger
import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.id.UuidGenerator
import ai.promoted.mockkRelaxedUnit
import ai.promoted.xray.NoOpXray
import com.google.protobuf.Message
import io.mockk.every
import io.mockk.verify
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TrackSessionUseCaseTest {
    private val enqueuedMessages = mutableListOf<Message>()
    private val logger = mockkRelaxedUnit<MetricsLogger> {
        every { enqueueMessage(capture(enqueuedMessages)) } returns Unit
    }

    private val trackUserUseCase = mockkRelaxedUnit<TrackUserUseCase> {
        every { currentOrNullUserId } returns ""
    }

    private val useCase = TrackSessionUseCase(
        systemLogger = SystemOutLogger(),
        logger = logger,
        idGenerator = UuidGenerator(),
        trackUserUseCase = trackUserUseCase,
        xray = NoOpXray()
    )

    @Test
    fun `Pre-emptive session ID is preserved after startSession`() {
        // Given
        val sessionIdBeforeStartSession = useCase.sessionId.currentOrPendingValue

        // When
        useCase.startSession("")

        // Then
        assertThat(useCase.sessionId.currentValue, equalTo(sessionIdBeforeStartSession))
    }

    @Test
    fun `New session ID is generated after second startSession`() {
        // Given a session already started
        useCase.startSession("")
        val firstSessionId = useCase.sessionId.currentValue

        // When a second session is started
        useCase.startSession("")
        val secondSessionId = useCase.sessionId.currentValue

        // Then
        assertThat(secondSessionId, not(firstSessionId))
    }

    @Test
    fun `User ID and logUserId are updated if user ID has changed`() {
        // When a session is started with a new user ID
        val newUserId = "${System.currentTimeMillis()}"
        useCase.startSession(newUserId)

        // Then
        verify(exactly = 1) {
            trackUserUseCase.setUserId(any(), newUserId)
        }
    }
}