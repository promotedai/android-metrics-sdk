package ai.promoted.metrics.usecases

import ai.promoted.metrics.id.UuidGenerator
import ai.promoted.mockkRelaxedUnit
import io.mockk.every
import io.mockk.verify
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class StartSessionUseCaseTest {
    private val logUser: LogUserUseCase = mockkRelaxedUnit()
    private val logSession: LogSessionUseCase = mockkRelaxedUnit()

    private val startSessionUseCase = StartSessionUseCase(
        idGenerator = UuidGenerator(),
        idStorageUseCase = mockkRelaxedUnit {
            every { currentUserId } returns ""
        },
        logUserUseCase = logUser,
        logSessionUseCase = logSession
    )

    @Test
    fun `Pre-emptive session ID is preserved after startSession`() {
        // Given
        val sessionIdBeforeStartSession = startSessionUseCase.sessionId

        // When
        startSessionUseCase.startSession("")

        // Then
        assertThat(startSessionUseCase.sessionId, equalTo(sessionIdBeforeStartSession))
    }

    @Test
    fun `New session ID is generated after second startSession`() {
        // Given a session already started
        startSessionUseCase.startSession("")
        val firstSessionId = startSessionUseCase.sessionId

        // When a second session is started
        startSessionUseCase.startSession("")
        val secondSessionId = startSessionUseCase.sessionId

        // Then
        assertThat(secondSessionId, not(firstSessionId))
    }

    @Test
    fun `User is logged after start session`() {
        // When a session is started
        startSessionUseCase.startSession("")

        // Then
        verify(exactly = 1) { logUser.logUser() }
    }

    @Test
    fun `Session is logged after start session`() {
        // When a session is started
        startSessionUseCase.startSession("")

        // Then
        verify(exactly = 1) { logSession.logSession() }
    }
}