package ai.promoted.sdk

import ai.promoted.AbstractContent
import ai.promoted.ActionData
import ai.promoted.ImpressionThreshold
import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.usecases.TrackActionUseCase
import ai.promoted.metrics.usecases.TrackImpressionsUseCase
import ai.promoted.metrics.usecases.TrackRVImpressionsUseCase
import ai.promoted.metrics.usecases.TrackSessionUseCase
import ai.promoted.metrics.usecases.TrackViewUseCase
import ai.promoted.proto.event.ActionType
import ai.promoted.xray.Xray
import androidx.recyclerview.widget.RecyclerView

/**
 * Default implementation of the [PromotedAiSdk] interface, which delegates each call to its
 * appropriate underlying use case. Calls to [shutdown] are delegated directly to the [logger].
 */
@Suppress("TooManyFunctions", "LongParameterList")
internal class DefaultSdk(
    private val logger: MetricsLogger,
    private val trackSessionUseCase: TrackSessionUseCase,
    private val trackViewUseCase: TrackViewUseCase,
    private val trackActionUseCase: TrackActionUseCase,
    private val trackImpressionsUseCase: TrackImpressionsUseCase,
    private val trackRVImpressionsUseCase: TrackRVImpressionsUseCase,
    private val xray: Xray
) : PromotedAiSdk {
    override fun startSession(userId: String) = trackSessionUseCase.startSession(userId)
    override fun onViewVisible(key: String) = trackViewUseCase.onViewVisible(key)
    override fun onAction(
        name: String,
        type: ActionType,
        dataBlock: (ActionData.Builder.() -> Unit)?
    ) = trackActionUseCase.onAction(name, type, dataBlock)

    override fun onAction(name: String, type: ActionType, data: ActionData) =
        trackActionUseCase.onAction(name, type, data)

    override fun onCollectionVisible(collectionViewKey: String, content: List<AbstractContent>) =
        trackImpressionsUseCase.onCollectionVisible(collectionViewKey, content)

    override fun onCollectionUpdated(collectionViewKey: String, content: List<AbstractContent>) =
        trackImpressionsUseCase.onCollectionUpdated(collectionViewKey, content)

    override fun onCollectionHidden(collectionViewKey: String) =
        trackImpressionsUseCase.onCollectionHidden(collectionViewKey)

    override fun trackRecyclerView(
        recyclerView: RecyclerView,
        currentDataProvider: () -> List<AbstractContent>,
        impressionThresholdBlock: (ImpressionThreshold.Builder.() -> Unit)?
    ) = trackRVImpressionsUseCase.trackRecyclerView(
        recyclerView, currentDataProvider, impressionThresholdBlock
    )

    override fun trackRecyclerView(
        recyclerView: RecyclerView,
        currentDataProvider: () -> List<AbstractContent>,
        impressionThreshold: ImpressionThreshold
    ) = trackRVImpressionsUseCase.trackRecyclerView(
        recyclerView, currentDataProvider, impressionThreshold
    )

    override fun inspectCaughtThrowables(): List<Throwable> = xray.caughtThrowables

    override fun shutdown() = logger.cancelAndDiscardPendingQueue()
}
