package ai.promoted.metrics.usecases

import ai.promoted.AbstractContent
import ai.promoted.calculation.AsyncCollectionDiffCalculator
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test

class AsyncCollectionDiffCalculatorTest {
    private val onResultListener = object {
        var results = mutableListOf<AsyncCollectionDiffCalculator.DiffResult<AbstractContent>>()
        val lastResult: AsyncCollectionDiffCalculator.DiffResult<AbstractContent>
            get() = results.lastOrNull() ?: AsyncCollectionDiffCalculator.DiffResult(
                emptyList(),
                emptyList()
            )

        fun onResult(result: AsyncCollectionDiffCalculator.DiffResult<AbstractContent>) {
            results.add(result)
        }
    }

    private val differ = AsyncCollectionDiffCalculator<AbstractContent>()

    @Test
    fun `Should be notified of new content after first diff`() {
        // Given
        val newBaseline = listOf(
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
        differ.scheduleDiffCalculation(
            newBaseline = newBaseline,
            onResult = onResultListener::onResult
        )
        // Crude delay because computation time should be short
        Thread.sleep(100L)

        // Then
        assertThat(onResultListener.lastResult.newItems, equalTo(newBaseline))
    }

    @Test
    fun `Should not be notified of repeated content after subsequent diff`() {
        // Given
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
        differ.scheduleDiffCalculation(
            newBaseline = originalContent,
            onResult = onResultListener::onResult
        )
        // Crude delay because computation time should be short
        Thread.sleep(100L)

        // When
        val newContent = originalContent.dropLast(1) + AbstractContent.Content("4")
        differ.scheduleDiffCalculation(
            newBaseline = newContent,
            onResult = onResultListener::onResult
        )
        Thread.sleep(100L)

        // Then
        assertThat(onResultListener.lastResult.newItems.size, equalTo(1))
        assertThat(
            onResultListener.lastResult.newItems.first(),
            equalTo(AbstractContent.Content("4"))
        )
    }

    @Test
    fun `Should be notified of dropped content after subsequent diff`() {
        // Given
        val contentToDrop = AbstractContent.Content(
            "3", "", ""
        )
        val originalContent = listOf(
            AbstractContent.Content(
                "1", "", ""
            ),
            AbstractContent.Content(
                "2", "", ""
            ),
            contentToDrop
        )
        differ.scheduleDiffCalculation(
            newBaseline = originalContent,
            onResult = onResultListener::onResult
        )
        // Crude delay because computation time should be short
        Thread.sleep(100L)

        // When
        val newContent = originalContent.dropLast(1) + AbstractContent.Content("4")
        differ.scheduleDiffCalculation(
            newBaseline = newContent,
            onResult = onResultListener::onResult
        )
        Thread.sleep(100L)

        // Then
        assertThat(onResultListener.lastResult.droppedItems.size, equalTo(1))
        assertThat(onResultListener.lastResult.droppedItems.first(), equalTo(contentToDrop))
    }

    /*
        Knowing that the internal implementation of the differ uses a CoroutineScope with a Mutex,
        ensure that the scope is not launching the jobs in a parallel fashion, but rather is
        guaranteeing execution order.
     */
    @Test
    fun `Should perform diffs sequentially and not concurrently`() {
        // Given
        val listOfContent = mutableListOf<AbstractContent>()
        repeat(1000) {
            listOfContent.add(AbstractContent.Content("$it"))
        }

        // When
        repeat(1000) { index ->
            differ.scheduleDiffCalculation(
                newBaseline = listOf(listOfContent[index]),
                onResult = onResultListener::onResult
            )
        }
        Thread.sleep(100L)

        // Then order is preserved
        assertThat(onResultListener.results.size, equalTo(listOfContent.size))
    }
}