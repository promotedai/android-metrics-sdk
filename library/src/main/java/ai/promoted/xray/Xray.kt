package ai.promoted.xray

internal interface Xray {
    val caughtThrowables: List<Throwable>

    fun <T : Any> monitored(block: () -> T): T
    suspend fun <T : Any> monitoredSuspend(block: suspend () -> T): T
}
