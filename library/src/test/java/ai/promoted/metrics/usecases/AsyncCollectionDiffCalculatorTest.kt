package ai.promoted.metrics.usecases

import ai.promoted.AbstractContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test

class AsyncCollectionDiffCalculatorTest {
    @Test
    fun `Should be notified of new content after first diff`() = runBlocking {
        // Given
        val differ = AsyncCollectionDiffCalculator<AbstractContent>()
        val newContent = listOf(
            AbstractContent.Content(
                "1", "", ""
            ),
            AbstractContent.Content(
                "2", "", ""
            ),
            AbstractContent.Content(
                "3", "", ""
            )
        )

        // When
        val verifiedContent = mutableListOf<AbstractContent>()
        differ.calculateDiff(
            newBaseline = newContent,
            onNewItem = {
                verifiedContent.add(it)
            }
        )
        // Crude delay because computation time should be short
        delay(100L)

        // Then
        assertThat(verifiedContent, equalTo(newContent))
    }

    @Test
    fun `Should not be notified of repeated content after subsequent diff`() = runBlocking {
        // Given
        val differ = AsyncCollectionDiffCalculator<AbstractContent>()
        val originalContent = listOf(
            AbstractContent.Content(
                "1", "", ""
            ),
            AbstractContent.Content(
                "2", "", ""
            ),
            AbstractContent.Content(
                "3", "", ""
            )
        )
        differ.calculateDiff(
            newBaseline = originalContent,
            onNewItem = {}
        )
        // Crude delay because computation time should be short
        delay(100L)

        // When
        val newContent = originalContent.dropLast(1) + AbstractContent.Content("4")
        val verifiedContent = mutableListOf<AbstractContent>()
        differ.calculateDiff(
            newBaseline = originalContent,
            onNewItem = {
                verifiedContent.add(it)
            }
        )
        delay(100L)

        // Then
        assertThat(newContent.last(), equalTo(AbstractContent.Content("4")))
    }

    /*
        Knowing that the internal implementation of the differ uses a CoroutineScope with a Mutex,
        ensure that the scope is not launching the jobs in a parallel fashion, but rather is
        guaranteeing execution order.
     */
    @Test
    fun `Should perform diffs sequentially and not concurrently`() = runBlocking {
        // Given
        val differ = AsyncCollectionDiffCalculator<AbstractContent>()
        val listOfContent = mutableListOf<AbstractContent>()
        repeat(1000) {
            listOfContent.add(AbstractContent.Content("$it"))
        }

        // When
        val verifiedContent = mutableListOf<AbstractContent>()
        repeat(1000) { index ->
            differ.calculateDiff(
                newBaseline = listOf(listOfContent[index]),
                onNewItem = { verifiedContent.add(it) }
            )
        }
        delay(100L)

        // Then order is preserved
        assertThat(verifiedContent.size, equalTo(listOfContent.size))
    }
}