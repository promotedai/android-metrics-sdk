package ai.promoted.metrics.usecases

import ai.promoted.AbstractContent
import ai.promoted.calculation.AsyncCollectionDiffCalculator
import ai.promoted.metrics.InternalImpressionData
import ai.promoted.metrics.MetricsLogger
import ai.promoted.platform.Clock
import kotlinx.coroutines.Dispatchers

/**
 * Allows you to track impressions based on a collection of [AbstractContent] (i.e. from a
 * RecyclerView or some other collection-based view).
 *
 * This class should be retained as a singleton to ensure the collection view keys are properly
 * tracked across their lifecycle.
 */
internal class TrackImpressionsUseCase(
    private val clock: Clock,
    private val logger: MetricsLogger,
    private val sessionUseCase: TrackSessionUseCase,
    private val viewUseCase: TrackViewUseCase,
    private val impressionIdGenerator: ImpressionIdGenerator
) {
    private val collectionDiffers =
        mutableMapOf<String, AsyncCollectionDiffCalculator<AbstractContent>>()

    /**
     * To be called when a collection view first becomes visible with its initial content. Only
     * for semantic clarity. Same as calling [onCollectionUpdated]
     */
    fun onCollectionVisible(collectionViewKey: String, visibleContent: List<AbstractContent>) =
        onCollectionUpdated(collectionViewKey, visibleContent)

    /**
     * To be called when the collection view with the given [collectionViewKey] has been entirely
     * removed/hidden from the viewport. This is so that the end-time of impressions that started
     * via [onCollectionUpdated] can be tracked.
     *
     * Only for semantic clarity. Same as calling [onCollectionUpdated] with an empty list.
     */
    fun onCollectionHidden(collectionViewKey: String) =
        onCollectionUpdated(collectionViewKey, emptyList())

    /**
     * To be called when the collection view with the given [collectionViewKey] has a
     * new list of *currently* visible content.
     *
     * Impressions will be tracked for every new piece of content. Content that existed in the
     * previous call to this function will not result in a new impression being logged.
     *
     * Content that was visible the last time this function was called, but is no longer visible,
     * will be considered "dropped"--and this will be tracked as the end of its total impression
     * time (start time = when it first shows up in [onCollectionUpdated], end time when
     * [onCollectionUpdated] is called again and the content is no longer present in the
     * [visibleContent] list provided.
     *
     * Note: The [visibleContent] parameter is not the full list of content backing the collection
     * view; rather, it should be a list representing the content/rows currently within the
     * viewport.
     */
    fun onCollectionUpdated(collectionViewKey: String, visibleContent: List<AbstractContent>) {
        if (visibleContent.isEmpty()) return onNoContent(collectionViewKey)

        val now = clock.currentTimeMillis
        val sessionId = sessionUseCase.sessionId
        val viewId = viewUseCase.viewId

        val differ = collectionDiffers.getOrPut(collectionViewKey) {
            AsyncCollectionDiffCalculator(
                calculationContext = Dispatchers.Default,
                notificationContext = Dispatchers.Main
            )
        }

        differ.scheduleDiffCalculation(
            newBaseline = visibleContent,
            onResult = { newDiff ->
                onNewDiff(
                    originalImpressionTime = now,
                    originalImpressionSessionId = sessionId,
                    originalImpressionViewId = viewId,
                    result = newDiff
                )
            }
        )
    }

    private fun onNoContent(collectionViewKey: String) {
        // If we begin logging end impressions, they would need to be handled from both the callback
        // passed to the differ, and also here
        collectionDiffers.remove(collectionViewKey)
    }

    private fun onNewDiff(
        originalImpressionTime: Long,
        originalImpressionSessionId: String,
        originalImpressionViewId: String,
        result: AsyncCollectionDiffCalculator.DiffResult<AbstractContent>
    ) {
        result.newItems.forEach { newContent ->
            onStartImpression(
                time = originalImpressionTime,
                sessionId = originalImpressionSessionId,
                viewId = originalImpressionViewId,
                content = newContent
            )
        }

        // Eventually, may iterate over dropped items and log end impressions
    }

    private fun onStartImpression(
        time: Long,
        sessionId: String,
        viewId: String,
        content: AbstractContent
    ) {
        val impressionId = impressionIdGenerator.generateImpressionId(
            insertionId = content.insertionId,
            contentId = content.contentId
        ) ?: return

        val impressionData = InternalImpressionData(
            time = time,
            sessionId = sessionId,
            viewId = viewId,
            impressionId = impressionId
        )

        logger.enqueueMessage(createImpressionMessage(impressionData))
    }
}
