package ai.promoted.metrics.usecases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Allows you to calculate diffs between successive collections of the same type, but in an
 * asynchronous fashion. Calculations are off-loaded to the [Dispatchers.Default] in order to
 * avoid blocking the UI/main thread with calculations.
 *
 * This is particularly useful in the case where you want to calculate diffs as a list view
 * scrolls.
 */
internal class AsyncCollectionDiffCalculator<ItemType : Any> {
    private val scope = CoroutineScope(Dispatchers.Default)
    private val mutex = Mutex()

    var currentBaseline = listOf<ItemType>()
        private set

    /**
     * Calculate the difference / deltas from the value of [newBaseline] last time this function was
     * called, to the new value of [newBaseline] being passed in on this call.
     *
     * @param onNewItem - granular notifications for when the calculator has found an item new to
     * the collection. Should return true or false indicating whether this new item should
     * @param onDroppedItem - granular notifications for when the calculator has found an item that
     * has been dropped from the collection (because the new value of [newBaseline] no longer
     * contains that item that was in the previous call's [newBaseline].
     *
     */
    fun calculateDiff(
        newBaseline: List<ItemType>,
        onNewItem: ((newItem: ItemType) -> Unit)? = null,
        onDroppedItem: ((droppedItem: ItemType) -> Unit)? = null
    ) {
        val lastBaseline = currentBaseline
        currentBaseline = newBaseline

        scope.launch {
            doLockingDiff(lastBaseline, newBaseline, onNewItem, onDroppedItem)
        }
    }

    /*
        Allows you to ensure calculations are performed in a sequential manner, so that no matter
        where or when this coroutine/suspending function was launched, it will be synchronously
        locked using a Mutex.
     */
    private suspend fun doLockingDiff(
        previousBaseline: List<ItemType>,
        newBaseline: List<ItemType>,
        onNewItem: ((newItem: ItemType) -> Unit)?,
        onDroppedItem: ((droppedItem: ItemType) -> Unit)?
    ) {
        mutex.withLock {
            processNewContent(previousBaseline, newBaseline, onNewItem)
            processDroppedContent(previousBaseline, newBaseline, onDroppedItem)
        }
    }

    private fun processNewContent(
        previousBaseline: List<ItemType>,
        newBaseline: List<ItemType>,
        onNewItem: ((newItem: ItemType) -> Unit)?,
    ) {
        if (onNewItem == null) return
        newBaseline.forEach { item ->
            if (!previousBaseline.contains(item)) onNewItem(item)
        }
    }

    private fun processDroppedContent(
        previousBaseline: List<ItemType>,
        newBaseline: List<ItemType>,
        onDroppedItem: ((droppedItem: ItemType) -> Unit)?
    ) {
        if (onDroppedItem == null) return
        previousBaseline.forEach { item ->
            if (!newBaseline.contains(item)) onDroppedItem(item)
        }
    }
}
