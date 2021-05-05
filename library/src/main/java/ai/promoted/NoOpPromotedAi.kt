package ai.promoted

import ai.promoted.metrics.AbstractContent
import ai.promoted.metrics.ActionData
import ai.promoted.proto.event.ActionType

/**
 * Non-operational implementation of the [PromotedAi] interface; used when
 * [ClientConfig.loggingEnabled] is set to false, or when the SDK has not yet been initialized.
 */
@Suppress("EmptyFunctionBlock")
internal class NoOpPromotedAi : PromotedAi {
    override fun startSession(userId: String) {}
    override fun onViewVisible(key: String) {}
    override fun onAction(
        name: String,
        type: ActionType,
        dataBlock: (ActionData.Builder.() -> Unit)?
    ) {
    }

    override fun onAction(name: String, type: ActionType, data: ActionData) {}

    override fun onCollectionVisible(collectionViewKey: String, content: List<AbstractContent>) {}
    override fun onCollectionHidden(collectionViewKey: String) {}

    override fun shutdown() {}
}
