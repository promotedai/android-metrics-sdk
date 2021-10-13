package ai.promoted.metrics.usecases

import ai.promoted.ImpressionData
import ai.promoted.metrics.InternalImpressionData
import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.id.IdGenerator
import ai.promoted.platform.Clock
import ai.promoted.xray.Xray
import android.app.Activity

/**
 * Allows you to manually track a single impression (as opposed to via collections with
 * [TrackCollectionsUseCase].
 *
 * This class can be constructed as needed and does not need to be a singleton; it is purely
 * functional. It does depend on a global viewId and sessionId, but those are provided by
 * the session & view use cases that should themselves be singletons.
 */
@Suppress("LongParameterList")
internal class TrackImpressionUseCase(
    private val clock: Clock,
    private val logger: MetricsLogger,
    private val idGenerator: IdGenerator,
    private val sessionUseCase: TrackSessionUseCase,
    private val viewUseCase: TrackViewUseCase,
    private val xray: Xray
) {
    /**
     * Logs an impression, along with any additional data associated to it (as provided by the
     * [dataBlock]). An example call would look like:
     *
     * onImpression {
     *     insertionId = "id"
     *     customProprties = SomeProps()
     * }
     *
     * The [dataBlock] is simply a lambda which receives an [ImpressionData.Builder] as its context,
     * so that you can granularly choose which additional data you want to be tied to this action.
     */
    fun onImpression(
        sourceActivity: Activity?,
        dataBlock: (ImpressionData.Builder.() -> Unit)?
    ) {
        val data = if (dataBlock != null) ImpressionData.Builder().apply(dataBlock).build(sourceActivity)
        else ImpressionData.Builder().build(sourceActivity)

        onImpression(data)
    }

    /**
     * Logs an impression, along with any additional data associated to it.
     */
    fun onImpression(data: ImpressionData) = xray.monitored {
        // Log a new view event if necessary
        data.sourceActivity?.let { viewUseCase.onImplicitViewVisible(it::class.java.name) }

        val impressionId = idGenerator.newId()

        // Allows for manual passing in of hasSuperImposedViews (i.e. via RN) or an inferred
        // value if one exists
        val hasSuperImposedViews = when (data.hasSuperImposedViews) {
            true -> true
            false -> false
            else -> data.sourceActivity?.hasWindowFocus() == false
        }

        val internalImpressionData = InternalImpressionData(
            time = clock.currentTimeMillis,
            impressionId = impressionId,
            sessionId = sessionUseCase.sessionId.currentValueOrNull,
            autoViewId = viewUseCase.autoViewId.currentValueOrNull,
            hasSuperImposedViews = hasSuperImposedViews
        )

        logger.enqueueMessage(
            createImpressionMessage(
                impressionData = data,
                internalImpressionData = internalImpressionData,
            )
        )
    }
}
