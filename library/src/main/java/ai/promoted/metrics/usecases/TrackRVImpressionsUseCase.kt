package ai.promoted.metrics.usecases

import ai.promoted.AbstractContent
import ai.promoted.RecyclerViewTracking
import ai.promoted.platform.Clock
import ai.promoted.ui.recyclerview.Tracker
import androidx.recyclerview.widget.RecyclerView

/**
 * Given a [RecyclerView], allows you to track impressions of [AbstractContent] based on which
 * rows are currently visible to the user. Will update impressions as the user scrolls.
 */
internal class TrackRVImpressionsUseCase(
    private val clock: Clock,
    private val coreImpressionsUseCase: TrackImpressionsUseCase
) {
    private val currentRecyclerViews = mutableMapOf<String, Tracker<AbstractContent>>()

    fun trackRecyclerView(
        recyclerView: RecyclerView,
        contentProvider: RecyclerViewTracking.ContentProvider,
        thresholdBlock: (RecyclerViewTracking.VisibilityThreshold.Builder.() -> Unit)?
    ) {
        val threshold = if (thresholdBlock != null)
            RecyclerViewTracking.VisibilityThreshold.Builder().apply(thresholdBlock).build()
        else RecyclerViewTracking.VisibilityThreshold.Builder().build()

        trackRecyclerView(recyclerView, contentProvider, threshold)
    }

    fun trackRecyclerView(
        recyclerView: RecyclerView,
        contentProvider: RecyclerViewTracking.ContentProvider,
        threshold: RecyclerViewTracking.VisibilityThreshold
    ) {
        val rvKey = "RV-${recyclerView.id}"

        // Cancel & remove any existing trackers for this RV
        stopAndRemoveTracking(rvKey)

        // Re-add a tracker (begins tracking automatically)
        currentRecyclerViews[rvKey] = Tracker(
            clock = clock,
            recyclerView = recyclerView,
            threshold = threshold,
            latestDataProvider = contentProvider::provideLatestData,
            onVisibleRowsChanged = { onVisibleRowsChanged(rvKey, it) },
            onRecyclerViewDetached = { onRVDetached(rvKey) }
        )
    }

    private fun onVisibleRowsChanged(rvKey: String, data: List<AbstractContent>) {
        coreImpressionsUseCase.onCollectionUpdated(rvKey, data)
    }

    private fun onRVDetached(rvKey: String) {
        stopAndRemoveTracking(rvKey)
        coreImpressionsUseCase.onCollectionHidden(rvKey)
    }

    private fun stopAndRemoveTracking(recyclerViewKey: String) =
        currentRecyclerViews.remove(recyclerViewKey)?.stopTracking()
}
