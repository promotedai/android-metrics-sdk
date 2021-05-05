package ai.promoted.metrics

internal data class ImpressionData(
    val time: Long,
    val sessionId: String,
    val viewId: String,
    val impressionId: String
)
