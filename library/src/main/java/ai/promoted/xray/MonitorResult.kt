package ai.promoted.xray

internal data class MonitorResult<T : Any>(
    val tag: String,
    val elapsedTimeMillis: Long,
    val functionReturn: FunctionReturn<T>
)
