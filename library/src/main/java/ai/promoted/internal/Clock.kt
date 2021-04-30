package ai.promoted.internal

internal interface Clock {
    val currentTimeMillis: Long
}

internal class SystemClock : Clock {
    override val currentTimeMillis: Long
        get() = System.currentTimeMillis()
}