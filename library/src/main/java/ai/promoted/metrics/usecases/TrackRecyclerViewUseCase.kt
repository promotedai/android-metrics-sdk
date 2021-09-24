package ai.promoted.metrics.usecases

import ai.promoted.AbstractContent
import ai.promoted.ImpressionThreshold
import ai.promoted.platform.Clock
import ai.promoted.ui.recyclerview.Tracker
import androidx.recyclerview.widget.RecyclerView

/**
 * Given a [RecyclerView], allows you to track impressions of [AbstractContent] based on which
 * rows are currently visible to the user. Will update impressions as the user scrolls.
 *
 * This class should be retained as a singleton to ensure the RecyclerView view keys are properly
 * tracked across their lifecycle.
 */
internal class TrackRecyclerViewUseCase(
    private val clock: Clock,
    private val coreImpressionsUseCase: TrackCollectionsUseCase
) {
    private val currentRecyclerViews = mutableMapOf<String, Tracker<AbstractContent>>()

    fun trackRecyclerView(
        recyclerView: RecyclerView,
        currentDataProvider: () -> List<AbstractContent>,
        impressionThresholdBlock: (ImpressionThreshold.Builder.() -> Unit)?
    ) {
        val threshold = if (impressionThresholdBlock != null)
            ImpressionThreshold.Builder().apply(impressionThresholdBlock).build()
        else ImpressionThreshold.Builder().build()

        trackRecyclerView(recyclerView, currentDataProvider, threshold)
    }

    fun trackRecyclerView(
        recyclerView: RecyclerView,
        currentDataProvider: () -> List<AbstractContent>,
        impressionThreshold: ImpressionThreshold
    ) {
        val rvKey = "RV-${recyclerView.id}"

        // Cancel & remove any existing trackers for this RV
        stopAndRemoveTracking(rvKey)

        // Re-add a tracker (begins tracking automatically)
        currentRecyclerViews[rvKey] = Tracker(
            clock = clock,
            recyclerView = recyclerView,
            visibilityThreshold = impressionThreshold,
            currentDataProvider = currentDataProvider,
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
