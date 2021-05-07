package ai.promoted

import ai.promoted.sdk.PromotedAiSdk
import androidx.recyclerview.widget.RecyclerView

/**
 * This class is primarily to provide an easy-to-use Java API, via
 * [PromotedAiSdk.buildRecyclerViewTracking], so that users of the library can dynamically configure
 * and initialize impression tracking of a RecyclerView.
 *
 * While this is supported for Kotlin users, the [PromotedAiSdk.trackRecyclerView]
 * function is recommended.
 */
class RecyclerViewTrackingBuilder internal constructor(
    private val sdk: PromotedAiSdk
) {
    private val visibilityThresholdBuilder = RecyclerViewTracking.VisibilityThreshold.Builder()

    fun withTimeThreshold(thresholdMillis: Long) = apply {
        visibilityThresholdBuilder.timeThresholdMillis = thresholdMillis
    }

    fun withPercentageThreshold(percentage: Double) = apply {
        visibilityThresholdBuilder.percentageThreshold = percentage
    }

    fun startTracking(
        recyclerView: RecyclerView,
        contentProvider: RecyclerViewTracking.ContentProvider
    ) {
        sdk.trackRecyclerView(
            recyclerView,
            contentProvider,
            visibilityThresholdBuilder.build()
        )
    }
}
