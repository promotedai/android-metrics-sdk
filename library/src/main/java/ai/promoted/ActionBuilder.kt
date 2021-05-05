package ai.promoted

import ai.promoted.proto.event.ActionType
import ai.promoted.sdk.PromotedAiSdk
import com.google.protobuf.Message

/**
 * This class is primarily to provide an easy-to-use Java API, via [PromotedAiSdk.buildAction], so that
 * users of the library can dynamically set whichever action components they want to set.
 *
 * While this is supported for Kotlin users, the [PromotedAiSdk.onAction] with the [ActionData.Builder]
 * configuration block is recommended.
 */
class ActionBuilder internal constructor(private val promotedAi: PromotedAiSdk) {
    private var name: String = ""
    private var type: ActionType = ActionType.UNKNOWN_ACTION_TYPE
    private val dataBuilder = ActionData.Builder()

    fun withName(name: String) = apply { this.name = name }
    fun withType(type: ActionType) = apply { this.type = type }
    fun withInsertionId(id: String) = apply { dataBuilder.insertionId = id }
    fun withContentId(id: String) = apply { dataBuilder.contentId = id }
    fun withRequestId(id: String) = apply { dataBuilder.requestId = id }
    fun withElementId(id: String) = apply { dataBuilder.elementId = id }
    fun withTargetUrl(url: String) = apply { dataBuilder.targetUrl = url }
    fun withCustomProperties(properties: Message) =
        apply { dataBuilder.customProperties = properties }

    fun log() = promotedAi.onAction(name, type, dataBuilder.build())
}
