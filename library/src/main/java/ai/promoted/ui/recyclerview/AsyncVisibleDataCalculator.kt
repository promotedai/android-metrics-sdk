package ai.promoted.ui.recyclerview

import ai.promoted.calculation.AsyncCalculationRunner
import ai.promoted.platform.Clock
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers

/**
 * Uses a [ConstrainedRowVisibilityCalculator] and an [AsyncCalculationRunner] to provide
 * asynchronous calculation of what data/content represented by a [RecyclerView] is currently
 * visible to the user.
 */
internal class AsyncVisibleDataCalculator<RowData : Any>(
    clock: Clock,
    recyclerView: RecyclerView,
    layoutManager: LinearLayoutManager,
    threshold: VisibilityThreshold,
    private val currentDataProvider: () -> List<RowData>,
    private val onVisibleRowsChanged: (rows: List<RowData>) -> Unit,
) {
    private val rowVisibilityCalculator = ConstrainedRowVisibilityCalculator(
        recyclerView = recyclerView,
        layoutManager = layoutManager,
        clock = clock,
        threshold = threshold
    )

    private val calculationRunner = AsyncCalculationRunner(
        calculationContext = Dispatchers.Default,
        notificationContext = Dispatchers.Main
    )

    fun calculateVisibleData() {
        val latestData = currentDataProvider.invoke()
        calculationRunner.scheduleCalculation(
            input = latestData,
            calculation = this::onExecuteVisibleDataCalculation,
            onResult = this.onVisibleRowsChanged
        )
    }

    private fun onExecuteVisibleDataCalculation(latestData: List<RowData>): List<RowData> {
        val rowVisibilities = rowVisibilityCalculator.calculateVisibleRows()

        return rowVisibilities
            .filter { it.position in latestData.indices }
            .map { latestData[it.position] }
    }
}
