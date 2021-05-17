package ai.promoted.xray

internal class NoOpXray : Xray {
    override val caughtThrowables: List<Throwable>
        get() = emptyList()

    override fun <T : Any> monitored(block: () -> T): T = block.invoke()
    override suspend fun <T : Any> monitoredSuspend(block: suspend () -> T): T = block.invoke()
}
