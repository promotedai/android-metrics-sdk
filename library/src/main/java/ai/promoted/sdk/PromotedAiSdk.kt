package ai.promoted.sdk

import ai.promoted.AbstractContent
import ai.promoted.ActionData
import ai.promoted.proto.event.ActionType

/**
 * The public-facing API for interacting with Promoted.Ai. Instances are managed internally by
 * the SDK.
 */
internal interface PromotedAiSdk {
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
