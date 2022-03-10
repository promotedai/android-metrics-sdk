package ai.promoted.metrics.usecases

import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.id.IdGenerator
import ai.promoted.mockkRelaxedUnit
import ai.promoted.proto.event.User
import ai.promoted.xray.NoOpXray
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TrackUserUseCaseTest {
    private val mockIdGenerator: IdGenerator = mockkRelaxedUnit {
        every { newId(any()) } returns "mock-generated-id"
    }
    private val mockCurrentUserIdsUseCase: CurrentUserIdsUseCase = mockkRelaxedUnit {
        every { currentLogUserId } returns ""
        every { currentUserId } returns ""
    }
    private val mockMetricsLogger: MetricsLogger = mockkRelaxedUnit()

    private val useCase = TrackUserUseCase(
        idGenerator = mockIdGenerator,
        clock = mockk { every { currentTimeMillis } returns 0L },
        currentUserIdsUseCase = mockCurrentUserIdsUseCase,
        xray = NoOpXray()
    )

    @Test
    fun `Should log user when new user ID set`() {
        // Given no IDs set
        every { mockCurrentUserIdsUseCase.currentLogUserId } returns ""
        every { mockCurrentUserIdsUseCase.currentUserId } returns ""

        // When set user ID
        useCase.setUserId(mockMetricsLogger, userId = "test-id")

        // Then log user is called with the user ID and
        verify {
            mockMetricsLogger.enqueueMessage(withArg {
                assertThat(actual, instanceOf(User::class.java))
                val userMessage = actual as User
                assertThat(userMessage.userInfo, notNullValue())
                val userInfoMessage = userMessage.userInfo!!
                assertThat(userInfoMessage.userId, equalTo("test-id"))
                assertThat(userInfoMessage.logUserId, equalTo("mock-generated-id"))
            })
        }
    }

    @Test
    fun `Should not log user when existing user ID set`() {
        // Given a current user ID has already been set
        every { mockCurrentUserIdsUseCase.currentLogUserId } returns ""
        every { mockCurrentUserIdsUseCase.currentUserId } returns "test-id"

        // When set user ID
        useCase.setUserId(mockMetricsLogger, userId = "test-id")

        // Then log user is not called
        verify(exactly = 0) { mockMetricsLogger.enqueueMessage(any<User>()) }
    }

    @Test
    fun `Should not log user when logUserId is overridden with blank value`() {
        // When override logUserId
        useCase.overrideLogUserId(mockMetricsLogger, logUserId = "")

        // Then log user is not called
        verify(exactly = 0) { mockMetricsLogger.enqueueMessage(any<User>()) }
    }
}