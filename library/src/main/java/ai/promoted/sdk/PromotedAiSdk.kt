package ai.promoted.sdk

import ai.promoted.AbstractContent
import ai.promoted.ActionData
import ai.promoted.RecyclerViewTracking
import ai.promoted.proto.event.ActionType
import androidx.recyclerview.widget.RecyclerView

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

    fun onCollectionUpdated(collectionViewKey: String, content: List<AbstractContent>)

    fun trackRecyclerView(
        recyclerView: RecyclerView,
        contentProvider: RecyclerViewTracking.ContentProvider,
        thresholdBlock: (RecyclerViewTracking.VisibilityThreshold.Builder.() -> Unit)? = null
    )

    fun trackRecyclerView(
        recyclerView: RecyclerView,
        contentProvider: RecyclerViewTracking.ContentProvider,
        threshold: RecyclerViewTracking.VisibilityThreshold
    )

    fun shutdown()
}
