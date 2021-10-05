package ai.promoted.sdk

import ai.promoted.AbstractContent
import ai.promoted.ActionData
import ai.promoted.metrics.MetricsLogger
import ai.promoted.metrics.usecases.TrackActionUseCase
import ai.promoted.metrics.usecases.TrackCollectionsUseCase
import ai.promoted.metrics.usecases.TrackRecyclerViewUseCase
import ai.promoted.metrics.usecases.TrackSessionUseCase
import ai.promoted.metrics.usecases.TrackViewUseCase
import ai.promoted.mockkRelaxedUnit
import ai.promoted.proto.event.ActionType
import io.mockk.verify
import org.junit.Test

class DefaultSdkTest {
    private val logger: MetricsLogger = mockkRelaxedUnit()
    private val sessionUseCase: TrackSessionUseCase = mockkRelaxedUnit()
    private val viewUseCase: TrackViewUseCase = mockkRelaxedUnit()
    private val actionUseCase: TrackActionUseCase = mockkRelaxedUnit()
    private val impressionUseCase: TrackCollectionsUseCase = mockkRelaxedUnit()
    private val rvImpressionsUseCase: TrackRecyclerViewUseCase = mockkRelaxedUnit()

    private val sdk = DefaultSdk(
        logger,
        mockkRelaxedUnit(),
        sessionUseCase,
        mockkRelaxedUnit(),
        viewUseCase,
        actionUseCase,
        impressionUseCase,
        rvImpressionsUseCase,
        mockkRelaxedUnit()
    )

    @Test
    fun startSession() {
        sdk.startSession("user-id")
        verify(exactly = 1) { sessionUseCase.startSession("user-id") }
    }

    @Test
    fun logView() {
        sdk.logView("view-id")
        verify(exactly = 1) { viewUseCase.logView("view-id") }
    }

    @Test
    fun onActionWithBuilder() {
        val dataBlock: ActionData.Builder.() -> Unit = {
            insertionId = "insertion-id"
        }
        sdk.onAction(null, "action-name", ActionType.CUSTOM_ACTION_TYPE, dataBlock)
        verify(exactly = 1) {
            actionUseCase.onAction(
                any(),
                "action-name",
                ActionType.CUSTOM_ACTION_TYPE,
                dataBlock
            )
        }
    }

    @Test
    fun onActionWithData() {
        val data = ActionData.Builder().apply {
            insertionId = "insertion-id"
        }.build(null)

        sdk.onAction("action-name", ActionType.CUSTOM_ACTION_TYPE, data)
        verify(exactly = 1) {
            actionUseCase.onAction(
                "action-name",
                ActionType.CUSTOM_ACTION_TYPE,
                data
            )
        }
    }

    @Test
    fun onCollectionVisible() {
        val collection = listOf(AbstractContent.Content("1"), AbstractContent.Content("2"))
        sdk.onCollectionVisible(null, "collection-key", collection)
        verify(exactly = 1) { impressionUseCase.onCollectionVisible(any(), "collection-key", collection) }
    }

    @Test
    fun onCollectionUpdated() {
        val collection = listOf(AbstractContent.Content("1"), AbstractContent.Content("2"))
        sdk.onCollectionUpdated(null, "collection-key", collection)
        verify(exactly = 1) { impressionUseCase.onCollectionUpdated(any(), "collection-key", collection) }
    }

    @Test
    fun onCollectionHidden() {
        sdk.onCollectionHidden(null, "collection-key")
        verify(exactly = 1) { impressionUseCase.onCollectionHidden(any(), "collection-key") }
    }

    @Test
    fun shutdown() {
        sdk.shutdown()
        verify(exactly = 1) { logger.cancelAndDiscardPendingQueue() }
    }
}