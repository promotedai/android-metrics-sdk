package ai.promoted

private const val FULL_VISIBILITY = 100.0

class RecyclerViewTracking {
    /**
     * Represents some class or object that can, on-demand, provide the latest list of [AbstractContent]
     * that is backing a RecyclerView.
     *
     * Used in order to specifically derive a subset of [AbstractContent] that represents the
     * data currently visible to the user.
     *
     * Note: For now, this requires that the RecyclerView has a single view-type that corresponds to
     * a view of the [AbstractContent].
     */
    interface ContentProvider {
        fun provideLatestData(): List<AbstractContent>
    }

    /**
     * Represents a declaration of what should be considered "visible" when tracking/calculating the
     * rows of [AbstractContent] that are currently visible to the user.
     */
    data class VisibilityThreshold(
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
            var timeThresholdMillis: Long = 1L,
            var percentageThreshold: Double = FULL_VISIBILITY
        ) {
            fun build() = VisibilityThreshold(
                timeThresholdMillis, percentageThreshold
            )
        }

        internal val allowsPartial: Boolean
            get() = percentageThreshold < FULL_VISIBILITY
    }
}
