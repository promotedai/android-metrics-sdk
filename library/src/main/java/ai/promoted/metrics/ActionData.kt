package ai.promoted.metrics

import ai.promoted.proto.event.ActionType

/**
 * Represents all data associated to an action that is provided by the library user.
 */
data class ActionData(
    val name: String,
    val type: ActionType,
    val insertionId: String?,
    val contentId: String?,
    val requestId: String?,
    val elementId: String?,
    val targetUrl: String?
)

/**
 * Represents all data associated to an action that is generated internally in the library.
 */
internal data class InternalActionData(
    val actionId: String,
    val sessionId: String,
    val viewId: String,
    val impressionId: String?
)
