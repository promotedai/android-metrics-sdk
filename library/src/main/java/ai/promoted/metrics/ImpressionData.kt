package ai.promoted.metrics

internal data class ImpressionData(
    val originalContent: AbstractContent,
    val time: Long,
    val sessionId: String,
    val viewId: String,
    val impressionId: String,
    val requestId: String?
)
