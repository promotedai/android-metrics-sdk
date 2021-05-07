package ai.promoted.ui.recyclerview

import ai.promoted.RecyclerViewTracking
import ai.promoted.platform.Clock
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Wraps around a [RowVisibilityCalculator] and applies the given
 * [RecyclerViewTracking.VisibilityThreshold] to the list of currently visible rows, so that only
 * those which meet the threshold are reported back upstream.
 */
internal class ConstrainedRowVisibilityCalculator(
    recyclerView: RecyclerView,
    layoutManager: LinearLayoutManager,
    private val clock: Clock,
    private val threshold: RecyclerViewTracking.VisibilityThreshold
) {
    private val rawVisibilityCalculator = RowVisibilityCalculator(
        clock, recyclerView, layoutManager
    )

    fun calculateVisibleRows(): List<RowVisibility> {
        val now = clock.currentTimeMillis

        val filteredRows = rawVisibilityCalculator
            .calculateRowVisibilities()
            .filter { visibility ->
                val timeVisible = now - visibility.visibleSinceMillis
                visibility.percentageVisible >= threshold.percentageThreshold
                        && timeVisible >= threshold.timeThresholdMillis
            }

        Log.v("RV", "Filtered rows: $filteredRows")
        return filteredRows
    }
}
