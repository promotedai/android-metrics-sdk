package ai.promoted.metrics.reference


/**
 * Allows us to read values for session IDs before starting a session and keep those IDs consistent
 * when the session does start.
 */
internal class IdProducer(
    private val producer: () -> String
) {
    private val initialValue: String by lazy(producer)

    private var lastNextValueRef: String? = null

    val currentValue: String
        get() = when (val lastNextValue = lastNextValueRef) {
            null -> initialValue
            else -> lastNextValue
        }

    fun nextValue(): String = when(lastNextValueRef) {
        null -> {
            // This is the first time nextValue() is being called, which means we should return
            // the initial value
            lastNextValueRef = initialValue
            initialValue
        }
        else -> {
            val nextValue = producer()
            lastNextValueRef = nextValue
            nextValue
        }
    }
}