package ai.promoted.ui.recyclerview

import ai.promoted.platform.Clock
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.min

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
        val globalVisibleRect = Rect()
        recyclerView.getGlobalVisibleRect(globalVisibleRect)

        val updatedExistingVisibilities =
            getUpdatedExistingVisibilities(globalVisibleRect, visibleRange).toMutableMap()

        addNewVisibilities(updatedExistingVisibilities, globalVisibleRect, visibleRange)

        rowVisibilities = updatedExistingVisibilities

        return rowVisibilities.values.toList()
    }

    private fun getUpdatedExistingVisibilities(
        globalVisibleRect: Rect,
        visibleRange: IntRange
    ) =
        rowVisibilities
            .filterKeys { position -> position in visibleRange }
            .mapValues { (position, visibility) ->
                val percentageVisible = getVisiblePercentageForPosition(globalVisibleRect, position)
                return@mapValues visibility.copy(percentageVisible = percentageVisible)
            }

    private fun addNewVisibilities(toMap: MutableMap<Int, RowVisibility>, globalVisibleRect: Rect, visibleRange: IntRange) {
        visibleRange.forEach { position ->
            toMap.putIfAbsent(
                position,
                RowVisibility(
                    position = position,
                    visibleSinceMillis = clock.currentTimeMillis,
                    percentageVisible = getVisiblePercentageForPosition(globalVisibleRect, position)
                )
            )
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

    private fun getVisiblePercentageForPosition(
        globalVisibleRect: Rect,
        position: Int
    ): Double {
        val view = layoutManager.findViewByPosition(position) ?: return 0.0
        val itemVisibleRect = Rect()
        view.getGlobalVisibleRect(itemVisibleRect)
        return getVisibleHeightPercentage(globalVisibleRect, itemVisibleRect, view)
    }

    private fun getVisibleHeightPercentage(
        globalVisibleRect: Rect,
        itemVisibleRect: Rect,
        view: View
    ): Double {
        val visibilityExtent = if (itemVisibleRect.bottom >= globalVisibleRect.bottom) {
            val visibleHeight = globalVisibleRect.bottom - itemVisibleRect.top
            min(visibleHeight.toFloat() / view.height, 1f)
        } else {
            val visibleHeight = itemVisibleRect.bottom - globalVisibleRect.top
            min(visibleHeight.toFloat() / view.height, 1f)
        }

        return (visibilityExtent * 100).toDouble()

//        val itemRect = Rect()
//        val isParentViewEmpty = view.getLocalVisibleRect(itemRect)
//
//        // Find the height of the item.
//        val visibleHeight = itemRect.height().toDouble()
//        val height = view.measuredHeight.toDouble()
//
//        val viewVisibleHeightPercentage = visibleHeight.percentageOf(height)
//
//        return if (isParentViewEmpty) viewVisibleHeightPercentage
//        else 0.0
    }

    @Suppress("MagicNumber")
    private fun Double.percentageOf(total: Double): Double = (this / total) * 100
}
