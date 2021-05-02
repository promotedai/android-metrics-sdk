package ai.promoted.metrics.usecases

import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.id.UuidGenerator
import ai.promoted.mockkRelaxedUnit
import ai.promoted.proto.event.Session
import ai.promoted.proto.event.User
import com.google.protobuf.Message
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TrackSessionUseCaseTest {
    private val enqueuedMessages = mutableListOf<Message>()
    private val logger = mockkRelaxedUnit<MetricsLogger> {
        every { enqueueMessage(capture(enqueuedMessages))} returns Unit
    }

    private val useCase = TrackSessionUseCase(
        clock = mockk { every { currentTimeMillis } returns 0L },
        logger = logger,
        idGenerator = UuidGenerator(),
        idStorageUseCase = mockkRelaxedUnit {
            every { currentUserId } returns ""
        }
    )

    @Test
    fun `Pre-emptive session ID is preserved after startSession`() {
        // Given
        val sessionIdBeforeStartSession = useCase.sessionId

        // When
        useCase.startSession("")

        // Then
        assertThat(useCase.sessionId, equalTo(sessionIdBeforeStartSession))
    }

    @Test
    fun `New session ID is generated after second startSession`() {
        // Given a session already started
        useCase.startSession("")
        val firstSessionId = useCase.sessionId

        // When a second session is started
        useCase.startSession("")
        val secondSessionId = useCase.sessionId

        // Then
        assertThat(secondSessionId, not(firstSessionId))
    }

    @Test
    fun `User is logged after start session`() {
        // When a session is started
        useCase.startSession("")

        // Then
        verify(exactly = 1) {
            logger.enqueueMessage(ofType(User::class))
        }
    }

    @Test
    fun `Session is logged after start session`() {
        // When a session is started
        useCase.startSession("")

        // Then
        verify(exactly = 1) {
            logger.enqueueMessage(ofType(Session::class))
        }
    }
}