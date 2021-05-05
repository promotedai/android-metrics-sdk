package ai.promoted.metrics.usecases

import ai.promoted.metrics.AbstractContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.*
import org.junit.Test

class AsyncContentDifferTest {
    @Test
    fun `Should be notified of new content after first diff` () = runBlocking {
        // Given
        val differ = AsyncContentDiffer()
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
        differ.diffContent(
            newBaseline = newContent,
            onNewContent = {
                verifiedContent.add(it)
                return@diffContent true
            }
        )
        // Crude delay because computation time should be short
        delay(100L)

        // Then
        assertThat(verifiedContent, equalTo(newContent))
    }

    @Test
    fun `Should only be notified of accepted new content after first diff` () = runBlocking {
        // Given
        val differ = AsyncContentDiffer()
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
        differ.diffContent(
            newBaseline = newContent,
            onNewContent = {
                // When
                if(it.name == "3") {
                    verifiedContent.add(it)
                    return@diffContent true
                } else return@diffContent false
            }
        )
        // Crude delay because computation time should be short
        delay(100L)

        // Then
        assertThat(verifiedContent.first(), equalTo(AbstractContent.Content("3","","")))
    }

    @Test
    fun `Should not be notified of repeated content after subsequent diff` () = runBlocking {
        // Given
        val differ = AsyncContentDiffer()
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
        differ.diffContent(
            newBaseline = originalContent,
            onNewContent = {
                // Accept all content
                return@diffContent true
            }
        )
        // Crude delay because computation time should be short
        delay(100L)

        // When
        val newContent = originalContent.dropLast(1) + AbstractContent.Content("4")
        val verifiedContent = mutableListOf<AbstractContent>()
        differ.diffContent(
            newBaseline = originalContent,
            onNewContent = {
                verifiedContent.add(it)
                return@diffContent true
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
        val differ = AsyncContentDiffer()
        val listOfContent = mutableListOf<AbstractContent>()
        repeat(1000) {
            listOfContent.add(AbstractContent.Content("$it"))
        }

        // When
        val verifiedContent = mutableListOf<AbstractContent>()
        repeat(1000) { index ->
            differ.diffContent(
                newBaseline = listOf(listOfContent[index]),
                onNewContent = {
                    verifiedContent.add(it)
                    return@diffContent true
                }
            )
        }
        delay(100L)

        // Then order is preserved
        assertThat(verifiedContent.size, equalTo(listOfContent.size))
    }
}