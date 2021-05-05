package ai.promoted

import ai.promoted.metrics.AbstractContent
import ai.promoted.metrics.ActionData
import ai.promoted.proto.event.ActionType

/**
 * The public-facing API for interacting with Promoted.Ai. Instances are managed internally by
 * the SDK.
 */
interface PromotedAiSdk {
    fun startSession(userId: String = "")
    fun onViewVisible(key: String)

    fun onAction(
        name: String,
        type: ActionType,
        dataBlock: (ActionData.Builder.() -> Unit)? = null
    )

    fun onAction(
        name: String,
        type: ActionType,
        data: ActionData
    )

    fun onCollectionVisible(collectionViewKey: String, content: List<AbstractContent>)
    fun onCollectionHidden(collectionViewKey: String)

    fun shutdown()
}
