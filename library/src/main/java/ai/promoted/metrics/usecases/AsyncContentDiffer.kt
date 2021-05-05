package ai.promoted.metrics.usecases

import ai.promoted.metrics.AbstractContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class AsyncContentDiffer {
    private val scope = CoroutineScope(Dispatchers.Default)
    private val mutex = Mutex()

    var currentBaseline = listOf<AbstractContent>()
        private set

    fun diffContent(
        newBaseline: List<AbstractContent>,
        onNewContent: ((newContent: AbstractContent) -> Boolean)? = null,
        onDroppedContent: ((droppedContent: AbstractContent) -> Unit)? = null
    ) {
        val lastBaseline = currentBaseline
        currentBaseline = newBaseline

        scope.launch {
            doLockingDiff(lastBaseline, newBaseline, onNewContent, onDroppedContent)
        }
    }

    private suspend fun doLockingDiff(
        previousBaseline: List<AbstractContent>,
        newBaseline: List<AbstractContent>,
        onNewContentItem: ((newContent: AbstractContent) -> Boolean)?,
        onDroppedContent: ((droppedContent: AbstractContent) -> Unit)?
    ) {
        mutex.withLock {
            processNewContent(previousBaseline, newBaseline, onNewContentItem)
            processDroppedContent(previousBaseline, newBaseline, onDroppedContent)
        }
    }

    private fun processNewContent(
        previousBaseline: List<AbstractContent>,
        newBaseline: List<AbstractContent>,
        onNewContentItem: ((newContent: AbstractContent) -> Boolean)?,
    ): List<AbstractContent> {
        val acceptedNewContent = mutableListOf<AbstractContent>()
        newBaseline.forEach { visibleContent ->
            if (onNewContentItem == null) return@forEach
            val isNew = !previousBaseline.contains(visibleContent)

            if (!isNew) return@forEach

            val isAccepted = onNewContentItem(visibleContent)
            if (isAccepted) acceptedNewContent.add(visibleContent)
        }

        return acceptedNewContent
    }

    private fun processDroppedContent(
        previousBaseline: List<AbstractContent>,
        newBaseline: List<AbstractContent>,
        onDroppedContent: ((droppedContent: AbstractContent) -> Unit)?
    ): List<AbstractContent> {
        val (previouslyAcceptedContent, droppedContent) =
            previousBaseline
                .partition { newBaseline.contains(it) }

        if (onDroppedContent != null) droppedContent.forEach { onDroppedContent.invoke(it) }

        return previouslyAcceptedContent
    }
}
