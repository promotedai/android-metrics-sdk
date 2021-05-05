package ai.promoted.metrics.usecases

import ai.promoted.metrics.id.IdGenerator
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Assert.assertThat
import org.junit.Test

class ImpressionIdGeneratorTest {
    private val randomUuid = "random-uuid"
    private val logUserId = "log-user-id"
    private val basedOnUuid = CapturingSlot<String>()
    private val idGenerator = mockk<IdGenerator> {
        every { newId() } returns randomUuid
        every { newId(basedOn = capture(basedOnUuid)) } answers {
            firstArg()
        }
    }
    private val currentUserIdsUseCase = mockk<CurrentUserIdsUseCase> {
        every { currentLogUserId } returns logUserId
    }

    private val impressionIdGenerator = ImpressionIdGenerator(idGenerator, currentUserIdsUseCase)

    @Test
    fun `Impression ID is based on insertion ID when it is available`() {
        // When
        val impressionId = impressionIdGenerator.generateImpressionId(
            insertionId = "test-insertion-id", contentId = "test-content-id"
        )

        // Then
        assertThat(impressionId, equalTo("test-insertion-id"))
    }

    @Test
    fun `Impression ID is based on combination of content ID and logUserId when content ID is available`() {
        // When
        // When
        val impressionId = impressionIdGenerator.generateImpressionId(
            insertionId = null, contentId = "test-content-id"
        )

        // Then
        assertThat(
            impressionId,
            equalTo("test-content-id$logUserId")
        )
    }
}