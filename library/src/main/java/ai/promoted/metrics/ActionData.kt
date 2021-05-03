package ai.promoted.metrics

import ai.promoted.proto.event.ActionType
import com.google.protobuf.Message

/**
 * Represents all data associated to an action that is provided by the library user.
 */
data class ActionData(
    val insertionId: String?,
    val contentId: String?,
    val requestId: String?,
    val elementId: String?,
    val targetUrl: String?,
    val customProperties: Message?
) {
    data class Builder(
        var insertionId: String? = null,
        var contentId: String? = null,
        var requestId: String? = null,
        var elementId: String? = null,
        var targetUrl: String? = null,
        var customProperties: Message? = null
    ) {
        fun build() = ActionData(
            insertionId, contentId, requestId, elementId, targetUrl, customProperties
        )
    }
}

/**
 * Represents all data associated to an action that is generated internally in the library.
 */
internal data class InternalActionData(
    val name: String,
    val type: ActionType,
    val actionId: String,
    val sessionId: String,
    val viewId: String,
    val impressionId: String?
)
