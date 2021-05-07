package ai.promoted.ui.recyclerview

import ai.promoted.RecyclerViewTracking
import ai.promoted.calculation.AsyncCalculationRunner
import ai.promoted.platform.Clock
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers

internal class VisibleDataCalculator<RowData : Any>(
    clock: Clock,
    recyclerView: RecyclerView,
    layoutManager: LinearLayoutManager,
    threshold: RecyclerViewTracking.VisibilityThreshold,
    private val latestDataProvider: () -> List<RowData>,
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
        val latestData = latestDataProvider.invoke()
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
            .mapIndexed { index, _ -> latestData[index] }
    }
}
