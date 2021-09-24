package ai.promoted.sdk

import ai.promoted.AbstractContent
import ai.promoted.ActionData
import ai.promoted.ImpressionData
import ai.promoted.ImpressionThreshold
import ai.promoted.proto.event.ActionType
import androidx.recyclerview.widget.RecyclerView

/**
 * Non-operational implementation of the [PromotedAiSdk] interface; used when
 * [ClientConfig.loggingEnabled] is set to false, or when the SDK has not yet been initialized.
 */
@Suppress("TooManyFunctions", "EmptyFunctionBlock")
internal class NoOpSdk : PromotedAiSdk {
    override var logUserId: String = ""
    override var sessionId: String = ""
    override var viewId: String = ""

    override fun startSession(userId: String) {}
    override fun onViewVisible(key: String) {}

    override fun onImpression(dataBlock: ImpressionData.Builder.() -> Unit) {
    }

    override fun onImpression(data: ImpressionData) {
    }

    override fun onAction(
        name: String,
        type: ActionType,
        dataBlock: (ActionData.Builder.() -> Unit)?
    ) {
    }

    override fun onAction(name: String, type: ActionType, data: ActionData) {}

    override fun onCollectionVisible(collectionViewKey: String, content: List<AbstractContent>) {}
    override fun onCollectionUpdated(collectionViewKey: String, content: List<AbstractContent>) {}
    override fun onCollectionHidden(collectionViewKey: String) {}

    override fun trackRecyclerView(
        recyclerView: RecyclerView,
        currentDataProvider: () -> List<AbstractContent>,
        impressionThresholdBlock: (ImpressionThreshold.Builder.() -> Unit)?
    ) {
    }

    override fun trackRecyclerView(
        recyclerView: RecyclerView,
        currentDataProvider: () -> List<AbstractContent>,
        impressionThreshold: ImpressionThreshold
    ) {
    }

    override fun inspectCaughtThrowables(): List<Throwable> = emptyList()

    override fun shutdown() {}
}
