package ai.promoted.metrics

import ai.promoted.proto.event.ActionType

/**
 * Represents all data associated to an action that is generated internally in the library.
 */
internal data class InternalActionData(
    val name: String,
    val type: ActionType,
    val actionId: String,
    val sessionId: String?,
    val autoViewId: String?,
    val impressionId: String?
)
