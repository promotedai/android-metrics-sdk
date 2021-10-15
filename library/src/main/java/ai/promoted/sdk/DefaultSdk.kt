package ai.promoted.sdk

import ai.promoted.AbstractContent
import ai.promoted.ActionData
import ai.promoted.AutoViewState
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
import android.app.Activity
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
        set(value) = trackUserUseCase.overrideLogUserId(logger, value)

    override var sessionId: String
        get() = trackSessionUseCase.sessionId.currentOrPendingValue
        set(value) = trackSessionUseCase.sessionId.override(value)

    override fun startSession(userId: String) = trackSessionUseCase.startSession(userId)

    override fun logView(viewId: String) = trackViewUseCase.logView(viewId)

    override fun logAutoView(
        autoViewId: String,
        routeName: String,
        routeKey: String
    ) = trackViewUseCase.logAutoView(autoViewId, routeName, routeKey)

    override fun onImpression(
        sourceActivity: Activity?,
        dataBlock: ImpressionData.Builder.() -> Unit
    ) = trackImpressionUseCase.onImpression(sourceActivity, dataBlock)

    override fun onImpression(data: ImpressionData) = trackImpressionUseCase.onImpression(data)

    override fun onAction(
        sourceActivity: Activity?,
        name: String,
        type: ActionType,
        dataBlock: (ActionData.Builder.() -> Unit)?
    ) = trackActionUseCase.onAction(sourceActivity, name, type, dataBlock)

    override fun onAction(name: String, type: ActionType, data: ActionData) =
        trackActionUseCase.onAction(name, type, data)

    override fun onCollectionVisible(
        sourceActivity: Activity?,
        collectionViewKey: String,
        content: List<AbstractContent>,
        autoViewState: AutoViewState?
    ) = trackCollectionsUseCase.onCollectionVisible(sourceActivity, collectionViewKey, content, autoViewState)

    override fun onCollectionUpdated(
        sourceActivity: Activity?,
        collectionViewKey: String,
        content: List<AbstractContent>,
        autoViewState: AutoViewState?
    ) = trackCollectionsUseCase.onCollectionUpdated(sourceActivity, collectionViewKey, content, autoViewState)

    override fun onCollectionHidden(
        sourceActivity: Activity?,
        collectionViewKey: String,
        autoViewState: AutoViewState?
    ) =
        trackCollectionsUseCase.onCollectionHidden(sourceActivity, collectionViewKey, autoViewState)

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
