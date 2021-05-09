package ai.promoted.xray

internal interface Xray {
    fun <T : Any> monitored(block: () -> T): T
    suspend fun <T : Any> monitoredSuspend(block: suspend () -> T): T
}
