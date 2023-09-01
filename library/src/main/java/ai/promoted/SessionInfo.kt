package ai.promoted

/**
 * Represents the current, top-level identifying information that is being used to track metrics
 * for a session.
 *
 * @see [PromotedAiSdk.currentSessionInfo]
 */
data class SessionInfo(
    val anonUserId: String,
    val sessionId: String,
    val viewId: String
)
