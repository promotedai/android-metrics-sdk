package ai.promoted.metrics

internal data class InternalImpressionData(
    val time: Long,
    val sessionId: String?,
    val autoViewId: String?,
    val impressionId: String,
    val hasSuperimposedViews: Boolean?
)
