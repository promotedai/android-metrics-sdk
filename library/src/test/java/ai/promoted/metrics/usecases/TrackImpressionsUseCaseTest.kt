package ai.promoted.metrics.usecases

import ai.promoted.AbstractContent
import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.id.AncestorId
import ai.promoted.metrics.id.IdGenerator
import ai.promoted.metrics.id.UuidGenerator
import ai.promoted.proto.event.Impression
import ai.promoted.xray.NoOpXray
import com.google.protobuf.Message
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class TrackImpressionsUseCaseTest {
    private val randomUuid = "random-uuid"
    private val logUserId = "log-user-id"
    private val testSessionId =
        AncestorId(UuidGenerator()).apply {
            override("session-id")
        }
    private val testViewId = "view-id"

    private val enqueuedMessages = mutableListOf<Message>()
    private val basedOnUuid = CapturingSlot<String>()
    private val logger = mockk<MetricsLogger> {
        every { enqueueMessage(capture(enqueuedMessages)) } returns Unit
    }
    private val idGenerator = mockk<IdGenerator> {
        every { newId() } returns randomUuid
        every { newId(basedOn = capture(basedOnUuid)) } answers {
            firstArg()
        }
    }
    private val currentUserIdsUseCase = mockk<CurrentUserIdsUseCase> {
        every { currentLogUserId } returns logUserId
    }
    private val useCase = TrackCollectionsUseCase(
        clock = mockk { every { currentTimeMillis } returns 0L },
        logger = logger,
        impressionIdGenerator = ImpressionIdGenerator(idGenerator, currentUserIdsUseCase),
        sessionUseCase = mockk {
            every { sessionId } returns testSessionId
        },
        viewUseCase = mockk {
            every { viewId } returns testViewId
        },
        xray = NoOpXray()
    )

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Default)
    }

    @Test
    fun `Impressions logged for new content`() = runBlocking {
        // Given
        val content = listOf(
            AbstractContent.Content("1", insertionId = "insertion-id"),
            AbstractContent.Content("2", insertionId = "insertion-id"),
            AbstractContent.Content("3", insertionId = "insertion-id"),
        )

        // When
        useCase.onCollectionUpdated("collection-key", content)
        delay(100L) // Give time for diff calculator

        // Then
        val impressions = enqueuedMessages.mapNotNull { it as? Impression }
        verify(exactly = content.size) { logger.enqueueMessage(any()) }
        assertThat(impressions.size, equalTo(content.size))
    }
}