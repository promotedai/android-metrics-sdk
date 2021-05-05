package ai.promoted.metrics.usecases

import ai.promoted.AbstractContent
import ai.promoted.metrics.InternalImpressionData
import ai.promoted.metrics.MetricsLogger
import ai.promoted.platform.Clock

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
     * To be called when the collection view with the given [collectionViewKey] has a
     * new list of *currently* visible content.
     *
     * Impressions will be tracked for every new piece of content. Content that existed in the
     * previous call to this function will not result in a new impression being logged.
     *
     * Content that was visible the last time this function was called, but is no longer visible,
     * will be considered "dropped"--and this will be tracked as the end of its total impression
     * time (start time = when it first shows up in [onCollectionVisible], end time when
     * [onCollectionVisible] is called again and the content is no longer present in the
     * [visibleContent] list provided.
     *
     * Note: The [visibleContent] parameter is not the full list of content backing the collection view;
     * rather, it should be a list representing the content/rows currently within the viewport.
     */
    fun onCollectionVisible(collectionViewKey: String, visibleContent: List<AbstractContent>) {
        val differ = collectionDiffers.getOrPut(collectionViewKey, ::AsyncCollectionDiffCalculator)
        val now = clock.currentTimeMillis
        val sessionId = sessionUseCase.sessionId
        val viewId = viewUseCase.viewId
        differ.calculateDiff(
            newBaseline = visibleContent,
            onNewItem = { newItem -> onStartImpression(now, sessionId, viewId, newItem) },
            // Currently not handling end impressions
            onDroppedItem = null
        )
    }

    /**
     * To be called when the collection view with the given [collectionViewKey] has been entirely
     * removed/hidden from the viewport. This is so that the end-time of impressions that started
     * via [onCollectionVisible] can be tracked.
     */
    fun onCollectionHidden(collectionViewKey: String) {
        // If we begin logging end impressions, they would need to be handled from both the callback
        // passed to the differ, and also here
        collectionDiffers.remove(collectionViewKey)
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
