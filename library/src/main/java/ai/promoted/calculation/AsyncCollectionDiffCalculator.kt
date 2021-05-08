package ai.promoted.calculation

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Allows you to calculate diffs between successive collections of the same type, but in an
 * asynchronous fashion. Calculations are off-loaded to the provided [CoroutineContext] in order to
 * allow one to avoid blocking the UI/main thread with calculations.
 *
 * This is particularly useful in the case where you want to calculate diffs as a list view
 * scrolls.
 */
internal class AsyncCollectionDiffCalculator<ItemType : Any>(
    calculationContext: CoroutineContext = Dispatchers.Default,
    notificationContext: CoroutineContext = calculationContext
) {
    private data class DiffInput<ItemType : Any>(
        val previousBaseline: List<ItemType>,
        val newBaseline: List<ItemType>
    )

    data class DiffResult<ItemType : Any>(
        val newItems: List<ItemType>,
        val droppedItems: List<ItemType>
    )

    private val asyncCalculationRunner = AsyncCalculationRunner(
        calculationContext = calculationContext,
        notificationContext = notificationContext
    )

    var currentBaseline = listOf<ItemType>()
        private set

    /**
     * Calculate the difference / deltas from the value of [newBaseline] last time this function was
     * called, to the new value of [newBaseline] being passed in on this call.
     *
     * The calculation is "scheduled" because it will be off-loaded to a background thread.
     */
    fun scheduleDiffCalculation(
        newBaseline: List<ItemType>,
        onResult: (result: DiffResult<ItemType>) -> Unit
    ) {
        val previousBaseline = currentBaseline
        currentBaseline = newBaseline

        val input = DiffInput(previousBaseline, newBaseline)

        asyncCalculationRunner.scheduleCalculation(
            input = input,
            calculation = this::calculateDiff,
            onResult = onResult
        )
    }

    private fun calculateDiff(input: DiffInput<ItemType>): DiffResult<ItemType> {
        val newItems = calculateNewItems(input.previousBaseline, input.newBaseline)
        val droppedItems = calculateDroppedItems(input.previousBaseline, input.newBaseline)
        return DiffResult(newItems, droppedItems)
    }

    private fun calculateNewItems(
        previousBaseline: List<ItemType>,
        newBaseline: List<ItemType>
    ): List<ItemType> {
        val newItems = mutableListOf<ItemType>()
        newBaseline.forEach { item ->
            if (!previousBaseline.contains(item)) newItems.add(item)
        }

        return newItems
    }

    private fun calculateDroppedItems(
        previousBaseline: List<ItemType>,
        newBaseline: List<ItemType>
    ): List<ItemType> {
        val droppedItems = mutableListOf<ItemType>()
        previousBaseline.forEach { item ->
            if (!newBaseline.contains(item)) droppedItems.add(item)
        }
        return droppedItems
    }
}
