package ai.promoted.sdk

import ai.promoted.AbstractContent
import ai.promoted.ActionData
import ai.promoted.ImpressionData
import ai.promoted.ImpressionThreshold
import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.usecases.TrackActionUseCase
import ai.promoted.metrics.usecases.TrackCollectionsUseCase
import ai.promoted.metrics.usecases.TrackImpressionUseCase
import ai.promoted.metrics.usecases.TrackRecyclerViewUseCase
import ai.promoted.metrics.usecases.TrackSessionUseCase
import ai.promoted.metrics.usecases.TrackUserUseCase
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
    private val trackUserUseCase: TrackUserUseCase,
    private val trackSessionUseCase: TrackSessionUseCase,
    private val trackImpressionUseCase: TrackImpressionUseCase,
    private val trackViewUseCase: TrackViewUseCase,
    private val trackActionUseCase: TrackActionUseCase,
    private val trackCollectionsUseCase: TrackCollectionsUseCase,
    private val trackRecyclerViewUseCase: TrackRecyclerViewUseCase,
    private val xray: Xray
) : PromotedAiSdk {
    override var logUserId: String
        get() = trackUserUseCase.currentOrPendingLogUserId
        set(value) = trackUserUseCase.overrideLogUserId(value)

    override var sessionId: String
        get() = trackSessionUseCase.sessionId.pendingOrCurrentValue
        set(value) = trackSessionUseCase.sessionId.override(value)

    override var viewId: String
        get() = trackViewUseCase.viewId.pendingOrCurrentValue
        set(value) = trackViewUseCase.viewId.override(value)

    override fun startSession(userId: String) = trackSessionUseCase.startSession(userId)
    override fun onViewVisible(key: String) = trackViewUseCase.onViewVisible(key)

    override fun onImpression(dataBlock: ImpressionData.Builder.() -> Unit) =
        trackImpressionUseCase.onImpression(dataBlock)

    override fun onImpression(data: ImpressionData) = trackImpressionUseCase.onImpression(data)

    override fun onAction(
        name: String,
        type: ActionType,
        dataBlock: (ActionData.Builder.() -> Unit)?
    ) = trackActionUseCase.onAction(name, type, dataBlock)

    override fun onAction(name: String, type: ActionType, data: ActionData) =
        trackActionUseCase.onAction(name, type, data)

    override fun onCollectionVisible(collectionViewKey: String, content: List<AbstractContent>) =
        trackCollectionsUseCase.onCollectionVisible(collectionViewKey, content)

    override fun onCollectionUpdated(collectionViewKey: String, content: List<AbstractContent>) =
        trackCollectionsUseCase.onCollectionUpdated(collectionViewKey, content)

    override fun onCollectionHidden(collectionViewKey: String) =
        trackCollectionsUseCase.onCollectionHidden(collectionViewKey)

    override fun trackRecyclerView(
        recyclerView: RecyclerView,
        currentDataProvider: () -> List<AbstractContent>,
        impressionThresholdBlock: (ImpressionThreshold.Builder.() -> Unit)?
    ) = trackRecyclerViewUseCase.trackRecyclerView(
        recyclerView, currentDataProvider, impressionThresholdBlock
    )

    override fun trackRecyclerView(
        recyclerView: RecyclerView,
        currentDataProvider: () -> List<AbstractContent>,
        impressionThreshold: ImpressionThreshold
    ) = trackRecyclerViewUseCase.trackRecyclerView(
        recyclerView, currentDataProvider, impressionThreshold
    )

    override fun inspectCaughtThrowables(): List<Throwable> = xray.caughtThrowables

    override fun shutdown() = logger.cancelAndDiscardPendingQueue()
}
