package ai.promoted.ui.recyclerview

import ai.promoted.RecyclerViewTracking
import ai.promoted.platform.Clock
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

        return rawVisibilityCalculator
            .calculateRowVisibilities()
            .filter { visibility ->
                val timeVisible = now - visibility.visibleSinceMillis
                visibility.percentageVisible >= threshold.percentageThreshold
                        && timeVisible >= threshold.timeThresholdMillis
            }
    }
}
