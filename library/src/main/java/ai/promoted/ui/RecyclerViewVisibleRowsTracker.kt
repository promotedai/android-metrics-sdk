package ai.promoted.ui

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers

/**
 * Given a [RecyclerView], this class allows you to be notified when the list of currently visible
 * rows changes.
 *
 * Credits for the code behind determining visibility percentage via:
 * https://stackoverflow.com/a/59085649
 */
internal class RecyclerViewVisibleRowsTracker(
    recyclerView: RecyclerView,
    private val thresholdConfig: VisibilityThresholdConfig,
    private val onVisibleRangeChanged: (range: IntRange?) -> Unit
) {
    data class VisibilityThresholdConfig(
        val timeThresholdMillis: Long,
        val percentageThreshold: Double = 100.0
    ) {
        internal val allowsPartial: Boolean
            get() = percentageThreshold < 100.0
    }

    private data class VisibilityCalculationInput(
        val recyclerView: RecyclerView,
        val layoutManager: LinearLayoutManager,
        val thresholdConfig: VisibilityThresholdConfig
    )

    private val calculator = RecyclerViewRowVisibilityCalculator()

    private val calculationRunner = AsyncCalculationRunner(
        notificationContext = Dispatchers.Main,
        calculation = this::calculate,
        onResult = this.onVisibleRangeChanged
    )

    init {
        when (val layoutManager = recyclerView.layoutManager) {
            is LinearLayoutManager -> addScrollListener(recyclerView, layoutManager)
        }
    }

    private fun addScrollListener(recyclerView: RecyclerView, layoutManager: LinearLayoutManager) {
        val calculationInput = VisibilityCalculationInput(
            recyclerView = recyclerView,
            layoutManager = layoutManager,
            thresholdConfig = thresholdConfig
        )

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recycler: RecyclerView, dx: Int, dy: Int) {
                calculationRunner.scheduleCalculation(calculationInput)
            }
        })
    }

    private fun calculate(input: VisibilityCalculationInput): IntRange? {
        return if (input.thresholdConfig.allowsPartial) calculateVisibilitiesWithinThreshold(input)
        else calculator.calculateOnlyCompletelyVisibleRows(input.layoutManager)
    }

    private fun calculateVisibilitiesWithinThreshold(input: VisibilityCalculationInput): IntRange? {
        val visibilitiesWithPercentages =
            calculator.calculateVisibleRowsWithPercentages(
                recyclerView = input.recyclerView,
                layoutManager = input.layoutManager
            )

        val indexOfFirstVisibleWithinThreshold =
            visibilitiesWithPercentages
                .keys
                .sorted()
                .firstOrNull { index ->
                    visibilitiesWithPercentages[index]?.let { visibilityPercentage ->
                        visibilityPercentage >= thresholdConfig.percentageThreshold
                    } ?: false
                } ?: return null

        val indexOfLastVisibleWithinThreshold =
            visibilitiesWithPercentages
                .keys
                .sorted()
                .lastOrNull { index ->
                visibilitiesWithPercentages[index]?.let { visibilityPercentage ->
                    visibilityPercentage >= thresholdConfig.percentageThreshold
                } ?: false
            } ?: return indexOfFirstVisibleWithinThreshold..indexOfFirstVisibleWithinThreshold

        return indexOfFirstVisibleWithinThreshold..indexOfLastVisibleWithinThreshold
    }
}
