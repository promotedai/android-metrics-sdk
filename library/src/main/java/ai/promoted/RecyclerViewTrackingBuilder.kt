package ai.promoted

import ai.promoted.sdk.PromotedAiSdk
import androidx.recyclerview.widget.RecyclerView

/**
 * This class is primarily to provide an easy-to-use Java API, via
 * [PromotedAi.buildRecyclerViewTracking], so that users of the library can dynamically configure
 * and initialize impression tracking of a RecyclerView.
 *
 * While this is supported for Kotlin users, the [PromotedAiSdk.trackRecyclerView]
 * function is recommended.
 */
class RecyclerViewTrackingBuilder internal constructor(
    private val sdk: PromotedAiSdk
) {
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
    interface CurrentDataProvider {
        fun provideCurrentData(): List<AbstractContent>
    }

    private val impressionThresholdBuilder = ImpressionThreshold.Builder()

    fun withTimeThreshold(thresholdMillis: Long) = apply {
        impressionThresholdBuilder.timeThresholdMillis = thresholdMillis
    }

    fun withPercentageThreshold(percentage: Double) = apply {
        impressionThresholdBuilder.percentageThreshold = percentage
    }

    fun startTracking(
        recyclerView: RecyclerView,
        currentDataProvider: CurrentDataProvider
    ) {
        sdk.trackRecyclerView(
            recyclerView,
            currentDataProvider::provideCurrentData,
            impressionThresholdBuilder.build()
        )
    }
}
