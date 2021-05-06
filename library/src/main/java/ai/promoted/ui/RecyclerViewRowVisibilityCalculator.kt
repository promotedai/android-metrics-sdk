package ai.promoted.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

internal class RecyclerViewRowVisibilityCalculator {
    fun calculateOnlyCompletelyVisibleRows(layoutManager: LinearLayoutManager): IntRange? =
        getVisiblePositionIntRange(
            layoutManager = layoutManager,
            allowPartialVisibility = false
        )

    fun calculateVisibleRowsWithPercentages(
        recyclerView: RecyclerView,
        layoutManager: LinearLayoutManager
    ): Map<Int, Double> {
        val visibleRange = getVisiblePositionIntRange(
            layoutManager = layoutManager,
            allowPartialVisibility = true
        ) ?: return emptyMap()

        recyclerView.getGlobalVisibleRect(Rect())

        val visiblePercentagesMap = mutableMapOf<Int, Double>()
        visibleRange.forEach { position ->
            val view = layoutManager.findViewByPosition(position) ?: return@forEach
            val percentage = getVisibleHeightPercentage(view)
            visiblePercentagesMap[position] = percentage
        }

        return visiblePercentagesMap
    }

    private fun getVisiblePositionIntRange(
        layoutManager: LinearLayoutManager,
        allowPartialVisibility: Boolean
    ): IntRange? {
        val firstPosition = if (allowPartialVisibility) layoutManager.findFirstVisibleItemPosition()
        else layoutManager.findFirstCompletelyVisibleItemPosition()

        if (firstPosition == RecyclerView.NO_POSITION) return null

        val lastPosition = if (allowPartialVisibility) layoutManager.findLastVisibleItemPosition()
        else layoutManager.findLastCompletelyVisibleItemPosition()

        return firstPosition..lastPosition
    }

    private fun getVisibleHeightPercentage(view: View): Double {
        val itemRect = Rect()
        val isParentViewEmpty = view.getLocalVisibleRect(itemRect)

        // Find the height of the item.
        val visibleHeight = itemRect.height().toDouble()
        val height = view.measuredHeight

        val viewVisibleHeightPercentage = visibleHeight / height * 100

        return if (isParentViewEmpty) viewVisibleHeightPercentage
        else 0.0
    }
}