package ai.promoted.ui.recyclerview

import ai.promoted.platform.Clock
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Allows you to determine the indices of all visible rows in a [RecyclerView]. You can either get
 * all entirely visible rows, or get a list of all rows with at least partial visibility
 * (accompanied by their visibility percentages).
 *
 * Credits for the code behind determining visibility percentage via:
 * https://stackoverflow.com/a/59085649
 */
internal class RowVisibilityCalculator(
    private val clock: Clock,
    private val recyclerView: RecyclerView,
    private val layoutManager: LinearLayoutManager
) {
    private var rowVisibilities = mapOf<Int, RowVisibility>()

    fun calculateRowVisibilities(): List<RowVisibility> {
        val visibleRange = getVisiblePositionIntRange() ?: return emptyList()

        // Required before calculating visibility of any child view of the RecyclerView
        recyclerView.getGlobalVisibleRect(Rect())

        val updatedExistingVisibilities =
            getUpdatedExistingVisibilities(visibleRange).toMutableMap()

        addNewVisibilities(updatedExistingVisibilities, visibleRange)

        rowVisibilities = updatedExistingVisibilities

        return rowVisibilities.values.toList()
    }

    private fun getUpdatedExistingVisibilities(visibleRange: IntRange) =
        rowVisibilities
            .filterKeys { position -> position in visibleRange }
            .mapValues { (position, visibility) ->
                val percentageVisible = getVisiblePercentageForPosition(position)
                return@mapValues visibility.copy(percentageVisible = percentageVisible)
            }

    private fun addNewVisibilities(toMap: MutableMap<Int, RowVisibility>, visibleRange: IntRange) {
        visibleRange.forEach { position ->
            if (!toMap.containsKey(position)) {
                toMap[position] = RowVisibility(
                    position = position,
                    visibleSinceMillis = clock.currentTimeMillis,
                    percentageVisible = getVisiblePercentageForPosition(position)
                )
            }
        }
    }

    private fun getVisiblePositionIntRange(): IntRange? {
        val firstPosition = layoutManager.findFirstVisibleItemPosition()
        return if (firstPosition != RecyclerView.NO_POSITION) {
            val lastPosition = layoutManager.findLastVisibleItemPosition()
            if (lastPosition != RecyclerView.NO_POSITION) firstPosition..lastPosition
            else null
        } else null
    }

    private fun getVisiblePercentageForPosition(position: Int): Double {
        val view = layoutManager.findViewByPosition(position) ?: return 0.0
        return getVisibleHeightPercentage(view)
    }

    private fun getVisibleHeightPercentage(view: View): Double {
        val itemRect = Rect()
        val isParentViewEmpty = view.getLocalVisibleRect(itemRect)

        // Find the height of the item.
        val visibleHeight = itemRect.height().toDouble()
        val height = view.measuredHeight.toDouble()

        val viewVisibleHeightPercentage = visibleHeight.percentageOf(height)

        return if (isParentViewEmpty) viewVisibleHeightPercentage
        else 0.0
    }

    @Suppress("MagicNumber")
    private fun Double.percentageOf(total: Double): Double = (this / total) * 100
}
