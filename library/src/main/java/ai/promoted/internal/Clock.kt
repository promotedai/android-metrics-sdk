package ai.promoted.internal

/**
 * Clock interface to allow for DI/testability
 */
internal interface Clock {
    val currentTimeMillis: Long
}

/**
 * Default implementation
 */
internal class SystemClock : Clock {
    override val currentTimeMillis: Long
        get() = System.currentTimeMillis()
}