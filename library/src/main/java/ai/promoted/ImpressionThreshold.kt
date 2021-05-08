package ai.promoted

private const val FULL_VISIBILITY = 100.0

/**
 * Represents a declaration of what should be considered "visible" when tracking/calculating the
 * rows of [AbstractContent] that are currently visible to the user.
 */
data class ImpressionThreshold(
    /**
     * TODO - not yet used
     * How long a row should be in the view-port until it is considered "seen"
     */
    val timeThresholdMillis: Long,

    /**
     * How much of the view must be in the view-port for it to be considered "seen"
     */
    val percentageThreshold: Double
) {
    data class Builder(
        var timeThresholdMillis: Long = 0L,
        var percentageThreshold: Double = FULL_VISIBILITY
    ) {
        fun build() = ImpressionThreshold(
            timeThresholdMillis, percentageThreshold
        )
    }
}
