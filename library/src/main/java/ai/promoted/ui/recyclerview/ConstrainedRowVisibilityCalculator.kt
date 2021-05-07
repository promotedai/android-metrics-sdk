package ai.promoted.ui.recyclerview

import ai.promoted.RecyclerViewTracking
import ai.promoted.platform.Clock
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
        // Raw visibilities must be calculated *before* the current time is taken, because
        // when raw visibilities are calculated for the first time, they will also be reading the
        // current time in order to keep track of the timestamp when they first became visible.
        // Therefore, if we read the current time prior to making this call, there will be a
        // negative different between "now" and when the rows became visible.
        val rawVisibilities = rawVisibilityCalculator.calculateRowVisibilities()

        val now = clock.currentTimeMillis

        return rawVisibilities
            .filter { visibility ->
                val timeVisible = now - visibility.visibleSinceMillis
                visibility.percentageVisible >= threshold.percentageThreshold
                        && timeVisible >= threshold.timeThresholdMillis
            }
    }
}
